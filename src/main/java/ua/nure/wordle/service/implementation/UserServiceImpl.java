package ua.nure.wordle.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.nure.wordle.dto.UserDTO;
import ua.nure.wordle.dto.UserGameDTO;
import ua.nure.wordle.dto.response.CabinetResponse;
import ua.nure.wordle.dto.response.GeneralStatisticResponse;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.UserGame;
import ua.nure.wordle.entity.enums.PlayerStatus;
import ua.nure.wordle.exception.EmailAlreadyExistsException;
import ua.nure.wordle.repository.UserRepository;
import ua.nure.wordle.service.interfaces.UserGameService;
import ua.nure.wordle.service.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserGameService userGameService;

    public UserServiceImpl(UserRepository userRepository, UserGameService userGameService) {
        this.userRepository = userRepository;
        this.userGameService = userGameService;
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " does not exist"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }

    @Override
    public List<GeneralStatisticResponse> getGeneralStatistic() {
        List<User> users = userRepository.findAllByOrderByCoinsTotalDesc();
        List<GeneralStatisticResponse> generalStatisticResponses = new ArrayList<>();
        for (User user : users) {
            generalStatisticResponses.add(GeneralStatisticResponse.builder()
                    .login(user.getLogin()).coinsTotal(user.getCoinsTotal()).build());
        }
        return generalStatisticResponses;
    }


    @Override
    public User create(User user) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(user.getEmail()))) {
            throw new EmailAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> readById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public User update(long id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User updateGameWinCount(long id, int attempts, PlayerStatus playerStatus) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        switch (playerStatus) {
            case WIN:
                user.setCoinsTotal(user.getCoinsTotal() + (7 - attempts));
                break;
            case LOSE:
                user.setCoinsTotal(user.getCoinsTotal() - 1);
                break;
            case DRAW:
                break;
            default:
                throw new IllegalArgumentException("Invalid player status: " + playerStatus);
        }
        return userRepository.save(user);
    }

    public Long getGameWinCount(int attempts, PlayerStatus playerStatus) {
        return switch (playerStatus) {
            case WIN -> (long) (7 - attempts);
            case LOSE -> (long) -1;
            case DRAW -> 0L;
            default -> throw new IllegalArgumentException("Invalid player status: " + playerStatus);
        };
    }

    public CabinetResponse getCabinet(Long id) {
        User existingUser = readById(id).
                orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        List<UserGame> userGames = userGameService.readByUserId(existingUser.getId());
        List<UserGameDTO> userGameDTOS = new ArrayList<>();
        int wins = 0;
        int loses = 0;
        for (UserGame userGame : userGames) {
            userGameDTOS.add(UserGameDTO.builder()
                    .date(userGame.getGame().getCreatedAt())
                    .word(userGame.getWord())
                    .playerStatus(PlayerStatus.valueOf(userGame.getPlayerStatus()))
                    .coins(getGameWinCount(userGame.getAttempts(),
                            PlayerStatus.valueOf(userGame.getPlayerStatus())))
                    .build());
            if (userGame.getPlayerStatus().equals("WIN")) {
                wins += 1;
            } else {
                loses += 1;
            }
        }
        return CabinetResponse.builder().user(UserDTO.builder()
                        .login(existingUser.getLogin())
                        .email(existingUser.getEmail())
                        .coinsTotal(existingUser.getCoinsTotal()).build())
                .userGames(userGameDTOS)
                .wins(wins)
                .losses(loses)
                .build();
    }

    @Override
    public User blockUser(User user) {
        if (Boolean.FALSE.equals(user.getIsBanned())) {
            user.setIsBanned(Boolean.TRUE);
        } else user.setIsBanned(Boolean.FALSE);
        return update(user.getId(), user);
    }

}

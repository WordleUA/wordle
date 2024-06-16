package ua.nure.wordle.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.nure.wordle.dto.UserDTO;
import ua.nure.wordle.dto.UserGameDTO;
import ua.nure.wordle.dto.response.AdministrationResponse;
import ua.nure.wordle.dto.response.CabinetResponse;
import ua.nure.wordle.dto.response.GeneralRatingResponse;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.enums.PlayerStatus;
import ua.nure.wordle.exception.EmailAlreadyExistsException;
import ua.nure.wordle.repository.UserGameRepository;
import ua.nure.wordle.repository.UserRepository;
import ua.nure.wordle.service.interfaces.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserGameRepository userGameRepository;

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача з email " + email + " не знайдено"));
    }
    public User getByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }

    @Override
    public List<GeneralRatingResponse> getGeneralRating() {
        return userRepository.findAllByOrderByCoinsTotalDesc().stream()
                .map(element -> modelMapper.map(element, GeneralRatingResponse.class))
                .toList();
    }

    @Override
    public List<AdministrationResponse> getUsersByAdmin() {
        return userRepository.findAll().stream()
                .map(element -> modelMapper.map(element, AdministrationResponse.class))
                .toList();
    }

    @Override
    public User getByConfirmationCode(String code) {
        return userRepository.findByConfirmationCode(code)
                .orElseThrow(() -> new EntityNotFoundException("User not found with confirmation code: " + code));
    }

    @Override
    public User getByPasswordResetCode(String passwordResetCode) {
        return userRepository.findByPasswordResetCode(passwordResetCode)
                .orElseThrow(() -> new EntityNotFoundException("User not found with password reset code: " + passwordResetCode));
    }

    @Override
    public boolean resetPassword(String passwordResetCode, String password) {
        User user = getByPasswordResetCode(passwordResetCode);
        if (user.getPasswordResetCode() == null || user.getPasswordResetCode().isBlank()
                || Boolean.TRUE.equals(user.getIsBanned())) {
            return false;
        }
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setPasswordResetCode(null);
        userRepository.save(user);
        return true;
    }

    @Override
    public User create(User user) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(user.getEmail()))) {
            throw new EmailAlreadyExistsException("Користувач з email " + user.getEmail() + " вже існує");
        }
        if (Boolean.TRUE.equals(userRepository.existsByLogin(user.getLogin()))) {
            throw new EmailAlreadyExistsException("Користувач з логіном " + user.getLogin() + " вже існує");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> readById(long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
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

    public Long calculateGameCoins(int attempts, PlayerStatus playerStatus) {
        return switch (playerStatus) {
            case WIN -> (long) (7 - attempts);
            case LOSE -> (long) -1;
            case DRAW -> 0L;
            default -> throw new IllegalArgumentException("Invalid player status: " + playerStatus);
        };
    }

    public CabinetResponse getCabinetInfo(User user) {
        List<UserGameDTO> userGames = userGameRepository.findByUserIdWithOpponentWord(user.getId());
        updateUserGamesWithCoins(userGames);
        return buildCabinetResponse(user, userGames);
    }

    private void updateUserGamesWithCoins(List<UserGameDTO> userGames) {
        userGames.forEach(userGameInfo -> {
            if (userGameInfo.getPlayerStatus() != null && userGameInfo.getAttempts() != null) {
                Long coins = calculateGameCoins(userGameInfo.getAttempts(), userGameInfo.getPlayerStatus());
                userGameInfo.setCoins(coins);
            }
        });
    }

    private static CabinetResponse buildCabinetResponse(User user, List<UserGameDTO> userGames) {
        return CabinetResponse.builder().
                user(UserDTO.builder()
                        .login(user.getLogin())
                        .email(user.getEmail())
                        .coinsTotal(user.getCoinsTotal())
                        .build())
                .userGames(userGames)
                .wins(user.getGameWinCount())
                .losses(user.getGameLoseCount())
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

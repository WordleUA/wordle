package ua.nure.wordle.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.enums.PlayerStatus;
import ua.nure.wordle.repository.UserRepository;
import ua.nure.wordle.service.interfaces.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " does not exist"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }

    @Override
    public User create(User user) {
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

    @Override
    public User blockUser(User user) {
        if(Boolean.FALSE.equals(user.getIsBanned())){
            user.setIsBanned(Boolean.TRUE);
        } else user.setIsBanned(Boolean.FALSE);
        return update(user.getId(), user);
    }

}

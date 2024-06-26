package ua.nure.wordle.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.nure.wordle.entity.UserGame;
import ua.nure.wordle.repository.UserGameRepository;
import ua.nure.wordle.service.interfaces.UserGameService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserGameServiceImpl implements UserGameService {

    private final UserGameRepository userGameRepository;


    @Override
    public UserGame create(UserGame userGame) {
        return userGameRepository.save(userGame);
    }

    @Override
    public Optional<UserGame> readById(long id) {
        return userGameRepository.findById(id);
    }

    @Override
    public UserGame update(long id, UserGame userGame) {
        return userGameRepository.save(userGame);
    }

    @Override
    public void delete(long id) {
        userGameRepository.deleteById(id);
    }

    @Override
    public List<UserGame> getAll() {
        return userGameRepository.findAll();
    }

    @Override
    public Page<UserGame> getAll(Pageable pageable) {
        return userGameRepository.findAll(pageable);
    }

    @Override
    public UserGame find(Long userId, Long gameId) {
        return userGameRepository.findByUserIdAndGameId(userId, gameId);
    }

    @Override
    public List<UserGame> readByUserId(Long userId) {
        return userGameRepository.findByUserId(userId);
    }

    @Override
    public UserGame update(UserGame userGame) {
        return userGameRepository.save(userGame);
    }

    public UserGame findOpponent(Long userId, Long gameId) {
        return userGameRepository.findByGameIdAndUserIdNot(gameId, userId);
    }
}
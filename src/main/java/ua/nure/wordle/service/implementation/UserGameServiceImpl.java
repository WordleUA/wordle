package ua.nure.wordle.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.nure.wordle.entity.UserGame;
import ua.nure.wordle.repository.UserGameRepository;
import ua.nure.wordle.service.interfaces.UserGameService;

import java.util.List;
import java.util.Optional;

@Service
public class UserGameServiceImpl implements UserGameService {
    private final UserGameRepository userGameRepository;

    public UserGameServiceImpl(UserGameRepository userGameRepository) {
        this.userGameRepository = userGameRepository;
    }

    @Override
    public UserGame create(UserGame userGame) {
        return null;
    }

    @Override
    public Optional<UserGame> readById(long id) {
        return Optional.empty();
    }

    @Override
    public UserGame update(long id, UserGame userGame) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public List<UserGame> getAll() {
        return List.of();
    }

    @Override
    public Page<UserGame> getAll(Pageable pageable) {
        return null;
    }
}

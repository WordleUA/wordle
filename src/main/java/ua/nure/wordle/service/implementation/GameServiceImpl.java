package ua.nure.wordle.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.nure.wordle.entity.Game;
import ua.nure.wordle.repository.GameRepository;
import ua.nure.wordle.service.interfaces.GameService;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {
    private GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game create(Game game) {
        return null;
    }

    @Override
    public Optional<Game> readById(long id) {
        return Optional.empty();
    }

    @Override
    public Game update(long id, Game game) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public List<Game> getAll() {
        return List.of();
    }

    @Override
    public Page<Game> getAll(Pageable pageable) {
        return null;
    }
}

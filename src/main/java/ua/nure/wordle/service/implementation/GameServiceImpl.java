package ua.nure.wordle.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.nure.wordle.entity.Game;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.UserGame;
import ua.nure.wordle.entity.UserGameId;
import ua.nure.wordle.entity.enums.GameStatus;
import ua.nure.wordle.repository.GameRepository;
import ua.nure.wordle.repository.UserRepository;
import ua.nure.wordle.service.interfaces.GameService;
import ua.nure.wordle.service.interfaces.UserGameService;
import ua.nure.wordle.websocket.GameWebSocketHandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final UserGameService userGameService;
    private final GameWebSocketHandler gameWebSocketHandler;

    public GameServiceImpl(GameRepository gameRepository, UserGameService userGameService, GameWebSocketHandler gameWebSocketHandler) {
        this.gameRepository = gameRepository;
        this.userGameService = userGameService;
        this.gameWebSocketHandler = gameWebSocketHandler;
    }

    @Override
    public Game create(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public Optional<Game> readById(long id) {
        return gameRepository.findById(id);
    }

    @Override
    public Game update(long id, Game game) {
        game.setId(id);
        return gameRepository.save(game);
    }

    @Override
    public void delete(long id) {
        gameRepository.deleteById(id);
    }

    @Override
    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    @Override
    public Page<Game> getAll(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    @Override
    public Optional<Game> findByStatus(GameStatus gameStatus) {
        return gameRepository.findByGameStatus(gameStatus);
    }

    @Override
    public Game connectGame(User user, String word) {
        Optional<Game> optionalGame = findByStatus(GameStatus.SEARCH);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            UserGame userGame = UserGame.builder()
                    .id(new UserGameId(user.getId(), game.getId()))
                    .game(game)
                    .user(user)
                    .playerStatus(null)
                    .word(word)
                    .attempts(null)
                    .build();
            game.setGameStatus(GameStatus.IN_PROGRESS);
            game.setStartedAt(Timestamp.from(Instant.now()));
            game = update(game.getId(), game);
            userGameService.create(userGame);
            gameWebSocketHandler.notifyGameStart(game.getId(), word);
            return game;
        } else {
            Game game = Game.builder()
                    .gameStatus(GameStatus.SEARCH)
                    .createdAt(Timestamp.from(Instant.now()))
                    .startedAt(null)
                    .endedAt(null)
                    .build();
            create(game);
            UserGame userGame = UserGame.builder()
                    .id(new UserGameId(user.getId(), game.getId()))
                    .game(game)
                    .user(user)
                    .playerStatus(null)
                    .word(word)
                    .attempts(null)
                    .build();
            userGameService.create(userGame);
            return game;
        }
    }

    @Override
    public Game updateEndTime(long id, Timestamp dateTime) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + id));
        game.setEndedAt(dateTime);
        return gameRepository.save(game);
    }

}
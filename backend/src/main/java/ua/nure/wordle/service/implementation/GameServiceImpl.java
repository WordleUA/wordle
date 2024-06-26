package ua.nure.wordle.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.nure.wordle.dto.request.EndGameRequest;
import ua.nure.wordle.dto.response.ConnectGameResponse;
import ua.nure.wordle.dto.response.EndGameResponse;
import ua.nure.wordle.entity.Game;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.UserGame;
import ua.nure.wordle.entity.UserGameId;
import ua.nure.wordle.entity.enums.GameStatus;
import ua.nure.wordle.entity.enums.PlayerStatus;
import ua.nure.wordle.repository.GameRepository;
import ua.nure.wordle.repository.UserGameRepository;
import ua.nure.wordle.repository.UserRepository;
import ua.nure.wordle.service.interfaces.GameService;
import ua.nure.wordle.service.interfaces.UserGameService;
import ua.nure.wordle.websocket.GameWebSocketHandler;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final UserGameService userGameService;
    private final GameWebSocketHandler gameWebSocketHandler;
    private final UserGameRepository userGameRepository;
    private final UserRepository userRepository;

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

    @Scheduled(cron = "0 0 3 * * *") //03:00 ночі
    @Transactional
    public void checkGameStatuses() {
        Instant tenMinutesAgo = Instant.now().minus(Duration.ofMinutes(10));
        List<Game> gamesInProgress = gameRepository.findAllByGameStatus(GameStatus.IN_PROGRESS);
        gamesInProgress.stream()
                .filter(game -> game.getStartedAt().isBefore(Timestamp.from(tenMinutesAgo).toLocalDateTime())).forEach(game -> {
                    game.setGameStatus(GameStatus.CANCELED);
                    game.setEndedAt(LocalDateTime.now());
                    game.getUserGames().forEach(userGame -> {
                        userGame.setPlayerStatus(PlayerStatus.DRAW);
                        userGame.setAttempts(0);
                        userGameService.update(userGame);
                    });
                    gameRepository.save(game);
                });
    }

    @Override
    public ConnectGameResponse connectGame(User user, String word) {
        List<Game> searchGames = gameRepository.findAllByGameStatus(GameStatus.SEARCH);
        user.setGameCount(user.getGameCount() + 1);
        userRepository.save(user);
        if (!searchGames.isEmpty()) {
            Game game = searchGames.get(0);

            UserGame userGame = UserGame.builder()
                    .id(new UserGameId(user.getId(), game.getId()))
                    .game(game)
                    .user(user)
                    .playerStatus(PlayerStatus.IN_GAME)
                    .word(word)
                    .attempts(null)
                    .build();
            userGameService.create(userGame);

            UserGame opponentUserGame = userGameService.findOpponent(user.getId(), game.getId());
            if (opponentUserGame != null) {
                opponentUserGame.setPlayerStatus(PlayerStatus.IN_GAME);
                userGameService.update(opponentUserGame);

                game.setGameStatus(GameStatus.IN_PROGRESS);
                game.setStartedAt(LocalDateTime.now());
                gameRepository.save(game);

                ConnectGameResponse connectGameSocketResponse = ConnectGameResponse.builder()
                        .gameId(game.getId())
                        .gameStatus(game.getGameStatus())
                        .opponentWord(word)
                        .build();

                ConnectGameResponse connectGameResponse = ConnectGameResponse.builder()
                        .gameId(game.getId())
                        .gameStatus(game.getGameStatus())
                        .opponentWord(opponentUserGame.getWord())
                        .build();

                gameWebSocketHandler.notifyGameStart(connectGameSocketResponse);
                return connectGameResponse;
            }
        }

        Game newGame = gameRepository.save(Game.builder()
                .gameStatus(GameStatus.SEARCH)
                .createdAt(LocalDateTime.now())
                .startedAt(null)
                .endedAt(null)
                .build());

        UserGame newUserGame = UserGame.builder()
                .id(new UserGameId(user.getId(), newGame.getId()))
                .playerStatus(PlayerStatus.WAITING)
                .game(newGame)
                .user(user)
                .word(word)
                .attempts(null)
                .build();

        userGameService.create(newUserGame);

        return ConnectGameResponse.builder()
                .gameId(newGame.getId())
                .gameStatus(newGame.getGameStatus())
                .build();
    }


    public void endGame(User user, EndGameRequest endGameRequest) {
        Game game = gameRepository.findById(endGameRequest.getGameId())
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + endGameRequest.getGameId()));
        if (game.getGameStatus() == GameStatus.IN_PROGRESS) {
            UserGame userGame = userGameService.find(user.getId(), endGameRequest.getGameId());
            UserGame opponentUserGame = userGameService.findOpponent(user.getId(), endGameRequest.getGameId());
            if (endGameRequest.getPlayerStatus().equals(PlayerStatus.WIN)) {
                userGame.setPlayerStatus(PlayerStatus.WIN);
                userGame.setAttempts(endGameRequest.getAttempts());
                opponentUserGame.setPlayerStatus(PlayerStatus.LOSE);
                user.setGameWinCount(user.getGameWinCount() + 1);
                user.setCoinsTotal(user.getCoinsTotal() + 7 - endGameRequest.getAttempts());
            } else if (endGameRequest.getPlayerStatus().equals(PlayerStatus.LOSE)) {
                userGame.setPlayerStatus(PlayerStatus.LOSE);
                userGame.setAttempts(endGameRequest.getAttempts());
                opponentUserGame.setPlayerStatus(PlayerStatus.WIN);
                user.setGameLoseCount(user.getGameLoseCount() + 1);
                if (user.getCoinsTotal() > 0) user.setCoinsTotal(user.getCoinsTotal() - 1);
            } else {
                userGame.setPlayerStatus(PlayerStatus.DRAW);
                userGame.setAttempts(0);
                opponentUserGame.setPlayerStatus(PlayerStatus.DRAW);
                opponentUserGame.setAttempts(0);
            }
            game.setGameStatus(GameStatus.COMPLETE);
            game.setEndedAt(LocalDateTime.now());
            gameRepository.save(game);
            userGameRepository.save(userGame);
            userGameRepository.save(opponentUserGame);
            userRepository.save(user);
            gameWebSocketHandler.notifyGameEnded(new EndGameResponse(game.getId(), opponentUserGame.getPlayerStatus()));
        } else if (game.getGameStatus() == GameStatus.COMPLETE) {
            if (endGameRequest.getPlayerStatus().equals(PlayerStatus.WIN)) {
                user.setGameWinCount(user.getGameWinCount() + 1);
                user.setCoinsTotal(user.getCoinsTotal() + 7 - endGameRequest.getAttempts());
            } else if (endGameRequest.getPlayerStatus().equals(PlayerStatus.LOSE)) {
                user.setGameLoseCount(user.getGameLoseCount() + 1);
                if (user.getCoinsTotal() > 0) user.setCoinsTotal(user.getCoinsTotal() - 1);
            }
            UserGame userGame = userGameService.find(user.getId(), endGameRequest.getGameId());
            userGame.setAttempts(endGameRequest.getAttempts());
            userGameRepository.save(userGame);
            userRepository.save(user);
        }
    }
}


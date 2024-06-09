package ua.nure.wordle.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.nure.wordle.dto.UserGameDTO;
import ua.nure.wordle.dto.response.ConnectGameResponse;
import ua.nure.wordle.dto.request.GameEndedSocketRequest;
import ua.nure.wordle.entity.Game;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.UserGame;
import ua.nure.wordle.entity.UserGameId;
import ua.nure.wordle.entity.enums.GameStatus;
import ua.nure.wordle.entity.enums.PlayerStatus;
import ua.nure.wordle.repository.GameRepository;
import ua.nure.wordle.repository.UserGameRepository;
import ua.nure.wordle.service.interfaces.GameService;
import ua.nure.wordle.service.interfaces.UserGameService;
import ua.nure.wordle.service.interfaces.UserService;
import ua.nure.wordle.utils.Patcher;
import ua.nure.wordle.websocket.GameWebSocketHandler;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final UserGameService userGameService;
    private final UserService userService;
    private final GameWebSocketHandler gameWebSocketHandler;
    private final Patcher<UserGame> userGamePatcher;

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
    public List<Game> findByStatus(GameStatus gameStatus) {
        return gameRepository.findAllByGameStatus(gameStatus);
    }

    @Scheduled(cron = "0 0 3 * * *") //03:00 ночі
    @Transactional
    public void checkGameStatuses() {
        Instant tenMinutesAgo = Instant.now().minus(Duration.ofMinutes(10));
        List<Game> gamesInProgress = gameRepository.findAllByGameStatus(GameStatus.IN_PROGRESS);
        gamesInProgress.stream()
                .filter(game -> game.getStartedAt().toInstant().isBefore(tenMinutesAgo))
                .forEach(game -> {
                    game.setGameStatus(GameStatus.CANCELED);
                    game.setEndedAt(Timestamp.from(Instant.now()));
                    game.getUserGames().forEach(userGame -> {
                        userGame.setPlayerStatus(PlayerStatus.DRAW.toString());
                        userGame.setAttempts(0);
                        userGameService.update(userGame);
                    });
                    gameRepository.save(game);
                });
    }

    @Override
    public ConnectGameResponse connectGame(User user, String word) {
        List<Game> searchGames = gameRepository.findAllByGameStatus(GameStatus.SEARCH);
        if (!searchGames.isEmpty()) {
            Game game = searchGames.get(0);
            UserGame userGame = UserGame.builder()
                    .id(new UserGameId(user.getId(), game.getId())).game(game)
                    .user(user).playerStatus(null).word(word).attempts(null).build();
            game.setGameStatus(GameStatus.IN_PROGRESS);
            game.setStartedAt(Timestamp.from(Instant.now()));
            game = update(game.getId(), game);
            userGameService.create(userGame);

            Optional<UserGame> opponentUserGame = game.getUserGames().stream()
                    .filter(ug -> !ug.getUser().getId().equals(user.getId()))
                    .findFirst();

            String opponentWord = opponentUserGame.map(UserGame::getWord).orElse(null);
            Long opponentId = opponentUserGame.map(ug -> ug.getUser().getId()).orElse(null);

            ConnectGameResponse connectGameSocketResponse = ConnectGameResponse.builder().gameId(game.getId())
                    .userId(opponentId).gameStatus(game.getGameStatus()).opponentWord(userGame.getWord()).build();

            ConnectGameResponse connectGameResponse = ConnectGameResponse.builder().gameId(game.getId())
                    .userId(user.getId()).gameStatus(game.getGameStatus()).opponentWord(opponentWord).build();

            gameWebSocketHandler.notifyGameStart(connectGameSocketResponse);
            return connectGameResponse;
        } else {
            Game game = Game.builder().gameStatus(GameStatus.SEARCH)
                    .createdAt(Timestamp.from(Instant.now())).startedAt(null).endedAt(null).build();
            UserGame userGame = UserGame.builder().id(new UserGameId(user.getId(), game.getId()))
                    .game(game).user(user).playerStatus(null).word(word).attempts(null).build();
            create(game);
            userGameService.create(userGame);
            return ConnectGameResponse.builder().gameId(game.getId()).userId(user.getId()).gameStatus(game.getGameStatus()).build();
        }
    }

    public void endGame(UserGameDTO userGameDTO, UserGame userGame, UserGame endedGame) throws IllegalAccessException {
        userGamePatcher.patch(userGame, endedGame);
        userGameService.update(userGame);
        userService.updateGameWinCount(userGame.getUser().getId(), userGame.getAttempts(), userGameDTO.getPlayerStatus());
        if (userGame.getGame().getGameStatus() == GameStatus.IN_PROGRESS) {
            Optional<UserGame> secondPlayer = userGameService.findSecondPlayer(userGameDTO.getGameId(), userGameDTO.getUserId());
            if(secondPlayer.isEmpty()) throw new EntityNotFoundException("UserGame not found with gameId: " + userGameDTO.getGameId() + ", userId: " + userGameDTO.getUserId());
            updateEndTime(userGameDTO.getGameId(), new Timestamp(System.currentTimeMillis()));
            updateIsGameOver(userGameDTO.getGameId(), GameStatus.COMPLETE);
            secondPlayer.get().determinePlayerStatus(userGameDTO.getPlayerStatus());
            userGameService.update(userGame.getGame().getId(), secondPlayer.orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + userGame.getGame().getId())));
            gameWebSocketHandler.notifyGameEnded(PlayerStatus.valueOf(secondPlayer.get().getPlayerStatus()), userGameDTO.getGameId());
        }
    }

    @Override
    public Game updateEndTime(long id, Timestamp dateTime) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + id));
        game.setEndedAt(dateTime);
        return gameRepository.save(game);
    }

    @Override
    public Game updateIsGameOver(long id, GameStatus isGameOver) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + id));
        game.setGameStatus(isGameOver);
        return gameRepository.save(game);
    }

}
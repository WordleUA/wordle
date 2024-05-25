package ua.nure.wordle.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ua.nure.wordle.dto.UserGameDTO;
import ua.nure.wordle.entity.Game;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.UserGame;
import ua.nure.wordle.entity.UserGameId;
import ua.nure.wordle.entity.enums.GameStatus;
import ua.nure.wordle.service.interfaces.GameService;
import ua.nure.wordle.service.interfaces.UserGameService;
import ua.nure.wordle.service.interfaces.UserService;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class GameWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;
    private final UserGameService userGameService;
    private final UserService userService;
    private ConcurrentHashMap<Long, Game> ongoingGames = new ConcurrentHashMap<>();

    public void handleJoinGame(UserGameDTO userGameDTO) {
        User user = userService.readById(userGameDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userGameDTO.getUserId()));

        Game game = findOrCreateGame(user, userGameDTO.getWord());

        if (game.getGameStatus() == GameStatus.SEARCH) {
            ongoingGames.put(user.getId(), game);
        } else {
            game.setGameStatus(GameStatus.IN_PROGRESS);
            game.setStartedAt(Instant.now());
            gameService.update(game.getId(), game);
            notifyPlayers(game);
        }
    }

    private Game findOrCreateGame(User user, String word) {
        Optional<Game> optionalGame = gameService.findByStatus(GameStatus.SEARCH);

        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            UserGame userGame = UserGame.builder()
                    .id(new UserGameId(user.getId(), game.getId()))
                    .game(game)
                    .user(user)
                    .playerStatus(null)
                    .word(word)
                    .isGameOver(false)
                    .attempts(null)
                    .build();
            game.setGameStatus(GameStatus.IN_PROGRESS);
            gameService.update(game.getId(), game);
            userGameService.create(userGame);
            return game;
        } else {
            Game newGame = Game.builder()
                    .gameStatus(GameStatus.SEARCH)
                    .createdAt(Instant.now())
                    .startedAt(null)
                    .endedAt(null)
                    .build();

            UserGame userGame = UserGame.builder()
                    .id(new UserGameId(user.getId(), newGame.getId()))
                    .game(newGame)
                    .user(user)
                    .playerStatus(null)
                    .word(word)
                    .isGameOver(false)
                    .attempts(null)
                    .build();
            gameService.create(newGame);
            userGameService.create(userGame);

            return newGame;
        }
    }


    private void notifyPlayers(Game game) {
        game.getUserGames().forEach(userGame -> {
            messagingTemplate.convertAndSendToUser(
                    userGame.getUser().getUsername(),
                    "/queue/start",
                    "start game"
            );
        });
    }
}


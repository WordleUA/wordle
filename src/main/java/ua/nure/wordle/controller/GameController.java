package ua.nure.wordle.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ua.nure.wordle.dto.GameDTO;
import ua.nure.wordle.dto.UserGameDTO;
import ua.nure.wordle.entity.Game;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.UserGame;
import ua.nure.wordle.entity.UserGameId;
import ua.nure.wordle.entity.enums.GameStatus;
import ua.nure.wordle.exceptions.NotFoundException;
import ua.nure.wordle.service.interfaces.GameService;
import ua.nure.wordle.service.interfaces.UserGameService;
import ua.nure.wordle.service.interfaces.UserService;
import ua.nure.wordle.utils.Patcher;
import ua.nure.wordle.websocket.GameWebSocketHandler;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final UserGameService userGameService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final Patcher<Game> patcher;
    private final GameWebSocketHandler gameWebSocketHandler;

    @GetMapping()
    public List<GameDTO> findAll() {
        return gameService.getAll().stream().map(this::convertToDTO).toList();
    }

    @GetMapping("/{id}")
    public GameDTO findById(@PathVariable Long id) {
        return convertToDTO(gameService.readById(id).
                orElseThrow(() -> new NotFoundException("Game not found with id: " + id)));
    }

    @PostMapping("/start")
    public GameDTO save(@RequestBody UserGameDTO userGameDTO) {
        User user = userService.readById(userGameDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userGameDTO.getUserId()));
        Optional<Game> optionalGame = gameService.findByStatus(GameStatus.SEARCH);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            UserGame userGame = UserGame.builder()
                    .id(new UserGameId(user.getId(), game.getId()))
                    .game(game)
                    .user(user)
                    .playerStatus(null)
                    .word(userGameDTO.getWord())
                    .attempts(null)
                    .build();
            game.setGameStatus(GameStatus.IN_PROGRESS);
            gameService.update(game.getId(), game);
            userGameService.create(userGame);
            gameWebSocketHandler.notifyGameStart(game.getId());
            return convertToDTO(game);
        } else {
            Game game = Game.builder()
                    .gameStatus(GameStatus.SEARCH)
                    .createdAt(Instant.now())
                    .startedAt(null)
                    .endedAt(null)
                    .build();
            gameService.create(game);
            UserGame userGame = UserGame.builder()
                    .id(new UserGameId(user.getId(), game.getId()))
                    .game(game)
                    .user(user)
                    .playerStatus(null)
                    .word(userGameDTO.getWord())
                    .attempts(null)
                    .build();
            userGameService.create(userGame);
            return convertToDTO(game);
        }
    }

    @PatchMapping("/{id}")
    public List<GameDTO> update(@PathVariable("id") Long id,
                                @RequestBody GameDTO gameDTO) {
        Game existingGame = gameService.readById(id).
                orElseThrow(() -> new NotFoundException("Game not found with id: " + id));
        Game updatedGame = convertToEntity(gameDTO);
        try {
            patcher.patch(existingGame, updatedGame);
            gameService.update(id, existingGame);
        } catch (IllegalAccessException e) {
            log.error("Error occurred while updating game with id: {}", id, e);
        }
        return findAll();
    }

    @DeleteMapping("/{id}")
    public List<GameDTO> delete(@PathVariable Long id) {
        gameService.delete(id);
        return findAll();
    }

    private UserGame convertToUserGame(UserGameDTO gameDTO, Game game, User user) {
        return UserGame.builder()
                .id(new UserGameId(user.getId(), game.getId()))
                .game(game)
                .user(user)
                .playerStatus(null)
                .word(gameDTO.getWord())
                .attempts(null)
                .build();
    }

    private Game buildGame() {
        return Game.builder()
                .gameStatus(GameStatus.SEARCH)
                .createdAt(Instant.now())
                .startedAt(null)
                .endedAt(null)
                .build();
    }

    private Game convertToEntity(GameDTO gameDTO) {
        return modelMapper.map(gameDTO, Game.class);
    }

    private GameDTO convertToDTO(Game game) {
        return modelMapper.map(game, GameDTO.class);
    }
}

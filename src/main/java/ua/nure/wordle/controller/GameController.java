package ua.nure.wordle.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ua.nure.wordle.dto.GameDTO;
import ua.nure.wordle.dto.response.GameEndedSocketRequest;
import ua.nure.wordle.dto.UserGameDTO;
import ua.nure.wordle.dto.request.ConnectGameRequest;
import ua.nure.wordle.dto.response.ConnectGameResponse;
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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
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
    private final Patcher<Game> gamePatcher;

    @GetMapping()
    public List<GameDTO> findAll() {
        return gameService.getAll().stream().map(this::convertToDTO).toList();
    }

    @GetMapping("/{id}")
    public GameDTO findById(@PathVariable Long id) {
        return convertToDTO(gameService.readById(id).
                orElseThrow(() -> new NotFoundException("Game not found with id: " + id)));
    }

    @PostMapping("/connect")
    public ConnectGameResponse connect(@RequestBody ConnectGameRequest connectGameRequest) {
        User user = userService.readById(connectGameRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + connectGameRequest.getUserId()));
        return gameService.connectGame(user, connectGameRequest.getWord());
    }

    @PatchMapping("/end")
    public void endGame(@RequestBody UserGameDTO userGameDTO) {
        UserGame userGame = userGameService.readById(userGameDTO.getUserId(), userGameDTO.getGameId())
                .orElseThrow(() -> new NotFoundException("UserGame not found with userId: " + userGameDTO.getUserId() + ", gameId: "));
        UserGame endedGame = convertToUserGame(userGameDTO);
        try {
            gameService.endGame(userGameDTO, userGame, endedGame);
        } catch (IllegalAccessException e) {
            log.error("Error occurred while updating userGame with id: {}, {}", userGameDTO.getGameId(), userGameDTO.getUserId(), e);
        }
    }

    @PatchMapping("/{id}")
    public List<GameDTO> update(@PathVariable("id") Long id,
                                @RequestBody GameDTO gameDTO) {
        Game existingGame = gameService.readById(id).
                orElseThrow(() -> new NotFoundException("Game not found with id: " + id));
        Game updatedGame = convertToEntity(gameDTO);
        try {
            gamePatcher.patch(existingGame, updatedGame);
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

    private UserGame convertToUserGame(UserGameDTO userGameDTO) {
        Game game = gameService.readById(userGameDTO.getGameId()).orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + userGameDTO.getGameId()));
        User user = userService.readById(userGameDTO.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userGameDTO.getUserId()));
        return UserGame.builder()
                .id(new UserGameId(userGameDTO.getUserId(), userGameDTO.getGameId()))
                .game(game)
                .user(user)
                .playerStatus(userGameDTO.getPlayerStatus())
                .word(userGameDTO.getWord())
                .attempts(userGameDTO.getAttempts())
                .build();
    }

    private Game buildGame() {
        return Game.builder()
                .gameStatus(GameStatus.SEARCH)
                .createdAt(Timestamp.from(Instant.now()))
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
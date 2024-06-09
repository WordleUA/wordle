package ua.nure.wordle.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua.nure.wordle.dto.GameDTO;
import ua.nure.wordle.dto.UserGameDTO;
import ua.nure.wordle.dto.request.ConnectGameRequest;
import ua.nure.wordle.dto.request.EndGameRequest;
import ua.nure.wordle.dto.response.ConnectGameResponse;
import ua.nure.wordle.entity.Game;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.UserGame;
import ua.nure.wordle.entity.UserGameId;
import ua.nure.wordle.service.interfaces.GameService;
import ua.nure.wordle.service.interfaces.UserGameService;
import ua.nure.wordle.service.interfaces.UserService;
import ua.nure.wordle.utils.Patcher;

import java.util.List;

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


    @PostMapping("/connect")
    public ConnectGameResponse connect(@AuthenticationPrincipal User user, @RequestBody ConnectGameRequest connectGameRequest) {
        return gameService.connectGame(user, connectGameRequest.getWord());
    }

    @PatchMapping("/end")
    public void endGame(@AuthenticationPrincipal User user, @RequestBody EndGameRequest endGameRequest) {
        gameService.endGame(user, endGameRequest);
    }


    private GameDTO convertToDTO(Game game) {
        return modelMapper.map(game, GameDTO.class);
    }
}
package ua.nure.wordle.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua.nure.wordle.dto.request.ConnectGameRequest;
import ua.nure.wordle.dto.request.EndGameRequest;
import ua.nure.wordle.dto.response.ConnectGameResponse;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.service.interfaces.GameService;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    @PostMapping("/connect")
    public ConnectGameResponse connect(@AuthenticationPrincipal User user, @RequestBody ConnectGameRequest connectGameRequest) {
        return gameService.connectGame(user, connectGameRequest.getWord());
    }

    @PatchMapping("/end")
    public void endGame(@AuthenticationPrincipal User user, @RequestBody EndGameRequest endGameRequest) {
        gameService.endGame(user, endGameRequest);
    }
}
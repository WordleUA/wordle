package ua.nure.wordle.service.interfaces;

import ua.nure.wordle.dto.UserGameDTO;
import ua.nure.wordle.dto.request.EndGameRequest;
import ua.nure.wordle.dto.response.ConnectGameResponse;
import ua.nure.wordle.entity.Game;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.UserGame;
import ua.nure.wordle.entity.enums.GameStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface GameService extends Service<Game>{
    ConnectGameResponse connectGame(User user, String word);
    void endGame(User user, EndGameRequest endGameRequest);

}
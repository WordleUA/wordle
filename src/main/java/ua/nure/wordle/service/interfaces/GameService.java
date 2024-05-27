package ua.nure.wordle.service.interfaces;

import ua.nure.wordle.entity.Game;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.enums.GameStatus;

import java.sql.Timestamp;
import java.util.Optional;

public interface GameService extends Service<Game>{
    Optional<Game> findByStatus(GameStatus gameStatus);
    Game connectGame(User user, String word);
    Game updateEndTime(long id, Timestamp dateTime);
}
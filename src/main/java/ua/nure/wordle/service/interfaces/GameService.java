package ua.nure.wordle.service.interfaces;

import ua.nure.wordle.entity.Game;
import ua.nure.wordle.entity.enums.GameStatus;

import java.util.Optional;

public interface GameService extends Service<Game>{
    Optional<Game> findByStatus(GameStatus gameStatus);
}

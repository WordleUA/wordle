package ua.nure.wordle.service.interfaces;

import ua.nure.wordle.entity.UserGame;

import java.util.List;
import java.util.Optional;

public interface UserGameService extends Service<UserGame>{

    List<UserGame> readByUserId(Long userId);
    UserGame update(UserGame userGame);
    UserGame find(Long userId, Long gameId);
    UserGame findOpponent(Long userId, Long gameId);
}
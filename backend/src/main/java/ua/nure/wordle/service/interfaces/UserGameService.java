package ua.nure.wordle.service.interfaces;

import ua.nure.wordle.entity.UserGame;

import java.util.List;
import java.util.Optional;

public interface UserGameService extends Service<UserGame>{
    Optional<UserGame> readById(Long userId, Long gameId);
    List<UserGame> readByUserId(Long userId);
    UserGame update(UserGame userGame);
    Optional<UserGame> findSecondPlayer(Long gameId, Long userId);
}
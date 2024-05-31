package ua.nure.wordle.service.interfaces;

import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.enums.PlayerStatus;

public interface UserService extends Service<User> {
    User updateGameWinCount(long id, int attempts, PlayerStatus playerStatus);
    User blockUser(User user);
}

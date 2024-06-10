package ua.nure.wordle.service.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.nure.wordle.dto.response.AdministrationResponse;
import ua.nure.wordle.dto.response.CabinetResponse;
import ua.nure.wordle.dto.response.GeneralStatisticResponse;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.enums.PlayerStatus;

import java.util.List;

public interface UserService extends Service<User> {
    User updateGameWinCount(User user, int attempts, PlayerStatus playerStatus);
    Long getGameWinCount(int attempts, PlayerStatus playerStatus);
    User blockUser(User user);
    CabinetResponse getCabinet(User user);
    User getByEmail(String email);
    UserDetailsService userDetailsService();
    List<GeneralStatisticResponse> getGeneralStatistic();
    List<AdministrationResponse> getUsersByAdmin();
    User getByConfirmationCode(String code);
    User getByPasswordResetCode(String passwordResetCode);
    boolean resetPassword(String passwordResetCode, String password);
}

package ua.nure.wordle.service.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.nure.wordle.dto.response.AdministrationResponse;
import ua.nure.wordle.dto.response.CabinetResponse;
import ua.nure.wordle.dto.response.GeneralRatingResponse;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.enums.PlayerStatus;

import java.util.List;

public interface UserService extends Service<User> {
    Long calculateGameCoins(int attempts, PlayerStatus playerStatus);
    User blockUser(User user);
    CabinetResponse getCabinetInfo(User user);
    User getByEmail(String email);
    User getByLogin(String login);
    UserDetailsService userDetailsService();
    List<GeneralRatingResponse> getGeneralRating();
    List<AdministrationResponse> getUsersByAdmin();
    User getByConfirmationCode(String code);
    User getByPasswordResetCode(String passwordResetCode);
    boolean resetPassword(String passwordResetCode, String password);
    Boolean existsByLogin(String login);
}

package ua.nure.wordle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ua.nure.wordle.entity.enums.UserRole;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    @Size(max = 45, message = "Username have to contain up to 45 symbols")
    String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    @Size(max = 45, message = "Email have to contain up to 45 symbols")
    String email;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    @Size(max = 45, message = "Password have to contain up to 45 symbols")
    @JsonProperty("password_hash")
    String passwordHash;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    UserRole role;

    @JsonProperty("is_banned")
    Boolean isBanned;

    @JsonProperty("game_win_count")
    Long gameWinCount;

    @JsonProperty("game_lose_count")
    Long gameLoseCount;

    @JsonProperty("game_count")
    Long gameCount;

    @JsonProperty("coins_total")
    Long coinsTotal;
}
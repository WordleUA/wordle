package ua.nure.wordle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
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
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    @Size(max = 45, message = "Username have to contain up to 45 symbols")
    private String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    @Size(max = 45, message = "Email have to contain up to 45 symbols")
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    @Size(max = 45, message = "Password have to contain up to 45 symbols")
    @JsonProperty("password_hash")
    private String passwordHash;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserRole role;

    @JsonProperty("is_banned")
    private Boolean isBanned;

    @JsonProperty("game_win_count")
    private Long gameWinCount;

    @JsonProperty("game_lose_count")
    private Long gameLoseCount;

    @JsonProperty("game_count")
    private Long gameCount;

    @JsonProperty("coins_total")
    private Long coinsTotal;
}
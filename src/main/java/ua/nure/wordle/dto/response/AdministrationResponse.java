package ua.nure.wordle.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministrationResponse {

    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    @NotNull
    private String login;

    @NotNull
    private String email;

    @NotNull
    private String role;

    @NotNull
    @JsonProperty("is_banned")
    private Boolean isBanned;

    @NotNull
    @JsonProperty("game_win_count")
    private Long gameWinCount;

    @NotNull
    @JsonProperty("game_lose_count")
    private Long gameLoseCount;

    @NotNull
    @JsonProperty("coins_total")
    private Long coinsTotal;
}

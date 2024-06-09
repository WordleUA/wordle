package ua.nure.wordle.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ua.nure.wordle.entity.enums.PlayerStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndGameResponse {
    @NotNull
    @JsonProperty("game_id")
    private Long gameId;

    @NotNull
    @JsonProperty("player_status")
    private PlayerStatus playerStatus;
}

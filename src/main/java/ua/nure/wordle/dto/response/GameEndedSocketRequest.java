package ua.nure.wordle.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ua.nure.wordle.entity.enums.PlayerStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameEndedSocketRequest {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("player_status")
    private PlayerStatus playerStatus;
}
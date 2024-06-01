package ua.nure.wordle.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ua.nure.wordle.entity.enums.PlayerStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameEndedSocketRequest {
    @JsonProperty("player_status")
    private PlayerStatus playerStatus;
}
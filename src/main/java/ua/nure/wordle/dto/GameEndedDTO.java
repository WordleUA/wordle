package ua.nure.wordle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ua.nure.wordle.entity.enums.PlayerStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameEndedDTO {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("player_status")
    private PlayerStatus playerStatus;
}
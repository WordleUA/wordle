package ua.nure.wordle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.nure.wordle.entity.enums.PlayerStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameEndedDTO {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("player_status")
    private PlayerStatus playerStatus;
    @JsonProperty("game_win_count")
    private Long gameWinCount;
}
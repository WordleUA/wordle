package ua.nure.wordle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class UserGameDTO {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("game_id")
    private Long gameId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("player_status")
    private PlayerStatus playerStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String word;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer attempts;
}
package ua.nure.wordle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ua.nure.wordle.entity.enums.PlayerStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGameDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("user_id")
    private Long userId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("game_id")
    private Long gameId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("player_status")
    private PlayerStatus playerStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String word;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer attempts;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime date;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long coins;

    public UserGameDTO(String opponentWord, LocalDateTime date, Integer attempts, PlayerStatus playerStatus) {
        this.word = opponentWord;
        this.date = date;
        this.attempts = attempts;
        this.playerStatus = playerStatus;
    }
}
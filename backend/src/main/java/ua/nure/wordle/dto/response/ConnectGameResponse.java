package ua.nure.wordle.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ua.nure.wordle.entity.enums.GameStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConnectGameResponse {
    @NotNull
    @JsonProperty("game_id")
    private Long gameId;

    @NotNull
    @JsonProperty("game_status")
    private GameStatus gameStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("opponent_word")
    @Size(max = 5)
    private String opponentWord;
}

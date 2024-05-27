package ua.nure.wordle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ua.nure.wordle.entity.enums.GameStatus;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO implements Serializable {
    private Long id;

    @NotNull
    private GameStatus gameStatus;

    @NotNull
    private Timestamp createdAt;

    private Timestamp startedAt;

    private Timestamp endedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<GameStartDTO> userGames = new LinkedHashSet<>();

}
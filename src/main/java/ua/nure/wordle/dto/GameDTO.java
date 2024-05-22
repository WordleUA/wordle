package ua.nure.wordle.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import ua.nure.wordle.entity.enums.GameStatus;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link ua.nure.wordle.entity.Game}
 */
@Value
public class GameDTO implements Serializable {
    Long id;
    @NotNull
    GameStatus gameStatus;
    @NotNull
    Instant createdAt;
    Instant startedAt;
    Instant endedAt;
}
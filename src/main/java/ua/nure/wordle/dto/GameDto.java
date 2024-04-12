package ua.nure.wordle.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link ua.nure.wordle.entity.Game}
 */
@Value
public class GameDto implements Serializable {
    Integer id;
    @NotNull
    String gameStatus;
    @NotNull
    Instant createdAt;
    Instant startedAt;
    Instant endedAt;
}
package ua.nure.wordle.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO implements Serializable {
    Integer id;

    @NotNull
    String gameStatus;

    @NotNull
    Instant createdAt;

    Instant startedAt;

    Instant endedAt;
}
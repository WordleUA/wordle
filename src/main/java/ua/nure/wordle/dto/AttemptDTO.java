package ua.nure.wordle.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptDTO implements Serializable {
    Integer id;

    @NotNull
    @Size(max = 6)
    String attemptedWord;

    @NotNull
    Instant startedAt;

    Instant endedAt;
}
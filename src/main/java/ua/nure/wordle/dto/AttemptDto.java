package ua.nure.wordle.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * DTO for {@link ua.nure.wordle.entity.Attempt}
 */
@Value
public class AttemptDto implements Serializable {
    Integer id;
    @NotNull
    @Size(max = 6)
    String attemptedWord;
    @NotNull
    LocalTime attemptTime;
}
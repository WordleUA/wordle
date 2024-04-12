package ua.nure.wordle.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link ua.nure.wordle.entity.UserGame}
 */
@Value
public class UserGameDto implements Serializable {
    Integer idUserId;
    Integer idGameId;
    UserDto user;
    GameDto game;
    @NotNull
    AttemptDto attempt;
    String playerStatus;
    @Size(max = 6)
    String word;
    @NotNull
    Byte isGameOver;
}
package ua.nure.wordle.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import ua.nure.wordle.entity.UserGameId;
import ua.nure.wordle.entity.enums.PlayerStatus;

import java.io.Serializable;

/**
 * DTO for {@link ua.nure.wordle.entity.UserGame}
 */
@Value
public class UserGameDTO implements Serializable {
    UserGameId id;
    UserDTO user;
    GameDTO game;
    PlayerStatus playerStatus;
    @Size(max = 6)
    String word;
    @NotNull
    Byte isGameOver;
    @NotNull
    Long attempts;
}
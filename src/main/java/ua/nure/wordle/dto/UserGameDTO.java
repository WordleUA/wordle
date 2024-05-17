package ua.nure.wordle.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGameDTO implements Serializable {
    Integer idUserId;

    Integer idGameId;

    UserDTO user;

    GameDTO game;

    String playerStatus;

    @Size(max = 6)
    String word;

    @NotNull
    Byte isGameOver;
}
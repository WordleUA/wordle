package ua.nure.wordle.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ua.nure.wordle.dto.UserDTO;
import ua.nure.wordle.dto.UserGameDTO;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.entity.UserGame;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CabinetResponse {
    @NotNull
    UserDTO user;
    @NotNull
    @JsonProperty("user_games")
    List<UserGameDTO> userGames;
    @NotNull
    Integer wins;
    @NotNull
    Integer losses;
}

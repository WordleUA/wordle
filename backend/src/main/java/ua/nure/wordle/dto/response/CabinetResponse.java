package ua.nure.wordle.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ua.nure.wordle.dto.UserDTO;
import ua.nure.wordle.dto.UserGameDTO;

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
    Long wins;
    @NotNull
    Long losses;
}

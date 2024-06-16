package ua.nure.wordle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ua.nure.wordle.entity.enums.GameStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO implements Serializable {
    private Long id;

    @NotNull
    private GameStatus gameStatus;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<UserGameDTO> userGames = new LinkedHashSet<>();

}
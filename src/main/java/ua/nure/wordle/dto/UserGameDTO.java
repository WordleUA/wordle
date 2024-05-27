package ua.nure.wordle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.nure.wordle.entity.enums.PlayerStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGameDTO {
    private Long userId;
    private Long gameId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PlayerStatus playerStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String word;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isGameOver;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer attempts;
}
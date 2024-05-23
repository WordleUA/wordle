package ua.nure.wordle.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGameDTO implements Serializable {
    @NotNull
//    @JsonProperty("user_id")
    private Long userId;

    @NotNull
    @Size (max = 5)
    private String word;
}
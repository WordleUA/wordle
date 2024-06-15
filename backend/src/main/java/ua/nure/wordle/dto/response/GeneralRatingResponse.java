package ua.nure.wordle.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralRatingResponse {

    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    @NotNull
    private String login;

    @NotNull
    @JsonProperty(value = "coins_total")
    private Long coinsTotal;
}

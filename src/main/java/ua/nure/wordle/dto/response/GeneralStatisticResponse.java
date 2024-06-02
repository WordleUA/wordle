package ua.nure.wordle.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralStatisticResponse {
    @NotNull
    private String login;

    @NotNull
    @JsonProperty(value = "coins_total")
    private Long coinsTotal;
}

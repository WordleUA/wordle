package ua.nure.wordle.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralStatisticResponse {
    @JsonProperty(required = true)
    private String login;

    @JsonProperty(value = "coins_total", required = true)
    private Long coinsTotal;
}

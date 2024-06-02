package ua.nure.wordle.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {

    @JsonProperty(required = true)
    Long id;

    @JsonProperty(required = true)
    String login;

    @JsonProperty(required = true)
    String email;
}
package ua.nure.wordle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.nure.wordle.entity.enums.UserRole;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    Integer id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    String password;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    UserRole role;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Boolean isBanned;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer gameWinCount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer gameCount;
}
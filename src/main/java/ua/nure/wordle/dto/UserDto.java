package ua.nure.wordle.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link ua.nure.wordle.entity.User}
 */
@Value
public class UserDto implements Serializable {
    Integer id;
    @NotNull
    @Size(max = 45)
    String username;
    @NotNull
    @Size(max = 45)
    String email;
    @NotNull
    @Size(max = 45)
    String password;
    @NotNull
    String role;
    @NotNull
    Byte isBanned;
    @NotNull
    Integer gameWinCount;
    @NotNull
    Integer gameCount;
}
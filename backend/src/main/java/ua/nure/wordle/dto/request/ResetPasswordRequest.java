package ua.nure.wordle.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {

    @NotNull
    @NotBlank(message = "Код відновлення паролю не може бути порожнім")
    @JsonProperty("password_reset_code")
    private String passwordResetCode;

    @NotNull
    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(max = 30, message = "Довжина пароля повинна бути не більше 30 символів")
    private String password;
}

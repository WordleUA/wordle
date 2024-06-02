package ua.nure.wordle.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Логін не може бути порожнім")
    @Size(max = 45, message = "Логін повинен бути не більше 45 символів")
    private String login;

    @NotBlank(message = "Email адреса не може бути порожньою")
    @Email(message = "Email адреса повинна бути дійсною")
    @Size(max = 100, message = "Довжина email адреси повинна бути не більше 100 символів")
    private String email;

    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(max = 255, message = "Довжина пароля повинна бути не більше 255 символів")
    private String password;
}

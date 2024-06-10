package ua.nure.wordle.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.wordle.dto.request.ResetPasswordRequest;
import ua.nure.wordle.dto.response.MessageResponse;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.service.EmailService;
import ua.nure.wordle.service.interfaces.UserService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class ResetPasswordController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestBody @Valid @NotNull @Email String email) {
        User user = userService.getByEmail(email);
        if (!user.isEnabled()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ви не підтвердили свою електронну адресу"));
        }
        if (Boolean.TRUE.equals(user.getIsBanned())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Ваш акаунт заблоковано"));
        }
        if (user.getPasswordResetCode() != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Запит на зміну пароля вже відправлено"));
        }
        user.setPasswordResetCode(RandomString.make(64));
        userService.update(user.getId(), user);
        emailService.sendResetPasswordEmail(user.getEmail(), user.getPasswordResetCode(), user.getLogin());
        return ResponseEntity.ok(new MessageResponse("Password reset email sent"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        if (userService.resetPassword(resetPasswordRequest.getPasswordResetCode(), resetPasswordRequest.getPassword())) {
            return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Password reset failed"));
        }
    }

}

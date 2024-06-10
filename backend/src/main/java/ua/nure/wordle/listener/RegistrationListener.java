package ua.nure.wordle.listener;

import lombok.NonNull;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.event.OnRegistrationCompleteEvent;
import ua.nure.wordle.service.EmailService;
import ua.nure.wordle.service.interfaces.UserService;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    private final UserService userService;
    private final EmailService emailService;

    @Value("${wordle.app.url}")
    private String appUrl;

    @Autowired
    public RegistrationListener(UserService userService, JavaMailSender mailSender, SpringTemplateEngine thymeleafTemplateEngine, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(@NonNull OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        user.setConfirmationCode(RandomString.make(64));
        userService.update(user.getId(), user);
        emailService.sendConfirmationEmail(user.getEmail(), user.getConfirmationCode(), user.getLogin());
    }
}

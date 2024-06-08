package ua.nure.wordle.listener;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import ua.nure.wordle.entity.User;
import ua.nure.wordle.event.OnRegistrationCompleteEvent;
import ua.nure.wordle.service.interfaces.UserService;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    private final UserService userService;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.from.name}")
    private String fromName;

    @Autowired
    public RegistrationListener(UserService userService, JavaMailSender mailSender, SpringTemplateEngine thymeleafTemplateEngine) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) throws MessagingException, UnsupportedEncodingException {
        User user = event.getUser();
        user.setVerificationCode(RandomString.make(64));
        userService.update(user.getId(), user);

        String recipientAddress = user.getEmail();
        String subject = "Підтвердіть вашу електронну адресу";
        String confirmationUrl = event.getAppUrl() + "/confirmRegistration/" + user.getVerificationCode();

        Map<String, Object> templateModel = Map.of(
                "confirmationUrl", confirmationUrl,
                "login", user.getLogin());

        sendMessageUsingThymeleafTemplate(recipientAddress, subject, templateModel);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(username, fromName);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }

    public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws MessagingException, UnsupportedEncodingException {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("confirm_email.html", thymeleafContext);
        sendHtmlMessage(to, subject, htmlBody);
    }
}

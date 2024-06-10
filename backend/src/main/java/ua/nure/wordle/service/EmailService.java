package ua.nure.wordle.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.from.name}")
    private String fromName;
    @Value("${wordle.app.url}")
    private String appUrl;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine thymeleafTemplateEngine) {
        this.mailSender = mailSender;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }

    public void sendConfirmationEmail(String recipientAddress, String confirmationCode, String login) {
        String subject = "Підтвердіть вашу електронну адресу";
        String confirmationUrl = appUrl + "/confirmRegistration/" + confirmationCode;
        Map<String, Object> templateModel = Map.of(
                "confirmationUrl", confirmationUrl,
                "login", login);

        sendMessageUsingThymeleafTemplate(recipientAddress, subject, templateModel, "confirm_email.html");
    }

    public void sendResetPasswordEmail(String recipientAddress, String resetPasswordCode, String login) {
        String subject = "Відновлення паролю на Wordle UA";
        String resetPasswordUrl = appUrl + "/passwordReset/" + resetPasswordCode;
        Map<String, Object> templateModel = Map.of(
                "resetPasswordUrl", resetPasswordUrl,
                "login", login);

        sendMessageUsingThymeleafTemplate(recipientAddress, subject, templateModel, "reset_password_email.html");
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


    private void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel, String templateFile) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process(templateFile, thymeleafContext);
        try {
            sendHtmlMessage(to, subject, htmlBody);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new MailSendException("Could not send email", e);
        }
    }
}

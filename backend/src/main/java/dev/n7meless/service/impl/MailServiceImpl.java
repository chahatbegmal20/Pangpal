package dev.n7meless.service.impl;

import dev.n7meless.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    @Value("${application.frontend.default-url}")
    private String defaultFrontendUrl;
    @Value("${spring.mail.username}")
    private String senderMail;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendForgotMessage(String email, String token, String baseUrl) {
        var url = baseUrl != null ? baseUrl : defaultFrontendUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderMail);
        message.setTo(email);
        message.setSubject("Reset your password");
        message.setText(String.format("Click %s/reset/%s to reset your password.", url, token));
        javaMailSender.send(message);
    }
}

package dev.n7meless.service;

public interface MailService {
    void sendForgotMessage(String email, String token, String baseUrl);
}

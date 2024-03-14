package dev.n7meless.dto.message;

public record MessageRequest(long first_user, long second_user, String text) {
}

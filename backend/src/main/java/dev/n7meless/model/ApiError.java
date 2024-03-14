package dev.n7meless.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
public class ApiError {
    private LocalDateTime time;
    private String message;
    private String error;

    public ApiError(String message, String error) {
        this.time = LocalDateTime.now();
        this.message = message;
        this.error = error;
    }
}

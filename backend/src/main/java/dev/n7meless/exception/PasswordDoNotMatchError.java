package dev.n7meless.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PasswordDoNotMatchError extends ResponseStatusException {

    public PasswordDoNotMatchError() {
        super(HttpStatus.BAD_REQUEST, "password do not match");
    }
}

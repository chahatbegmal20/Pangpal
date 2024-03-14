package dev.n7meless.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidLinkError extends ResponseStatusException {
    public InvalidLinkError() {
        super(HttpStatus.BAD_REQUEST, "invalid link error");
    }
}

package dev.n7meless.handler;

import dev.n7meless.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class HandleErrorService {

//    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiError> unexpectedExceptionHandler() {
        return new ResponseEntity<>(new ApiError(
                "An occurred error when processing request",
                HttpStatus.BAD_REQUEST.getReasonPhrase()),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler
    public ResponseEntity<ApiError> noSuchElementExceptionHandler(NoSuchElementException ex) {
        return new ResponseEntity<>(new ApiError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase()),
                HttpStatus.BAD_REQUEST
        );
    }
}

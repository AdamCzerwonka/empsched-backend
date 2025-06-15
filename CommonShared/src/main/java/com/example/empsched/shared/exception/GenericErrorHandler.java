package com.example.empsched.shared.exception;

import com.example.empsched.shared.dto.Problem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GenericErrorHandler {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Problem> handleApplicationException(ApplicationException ex) {
        log.error(ex.getMessage(), ex);

        Problem problem = new Problem(ex.getMessage(), ex.getMessageKey());

        return ResponseEntity.status(ex.getStatusCode()).body(problem);
    }
}

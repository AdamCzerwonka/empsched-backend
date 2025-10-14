package com.example.empsched.shared.exception;

import com.example.empsched.shared.dto.Problem;
import io.temporal.client.WorkflowFailedException;
import io.temporal.failure.ApplicationFailure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(WorkflowFailedException.class)
    public ResponseEntity<?> handleWorkflowFailedException(WorkflowFailedException ex) {
        log.error(ex.getMessage());
        try {
            if (ex.getCause() != null && ex.getCause().getCause() != null
                    && ex.getCause().getCause() instanceof ApplicationFailure af) {
                final int statusCode = af.getDetails().get(0, Integer.class);
                final String responseBody = af.getDetails().get(1, String.class);

                return ResponseEntity.status(statusCode).body(responseBody);
            }
        } catch (Exception ex1) {
            log.error("Error while handling WorkflowFailedException", ex1);
        }
        Problem problem = new Problem("Internal server error", "default");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }
}

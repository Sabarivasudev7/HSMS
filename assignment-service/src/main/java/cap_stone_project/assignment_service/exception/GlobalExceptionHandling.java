package cap_stone_project.assignment_service.exception;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(AssignmentNotFoundException.class)
    public ResponseEntity<String> handleAssignmentException(AssignmentNotFoundException ex){

        return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(ex.getMessage());
    }
}
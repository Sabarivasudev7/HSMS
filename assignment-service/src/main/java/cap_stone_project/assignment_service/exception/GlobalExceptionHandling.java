package cap_stone_project.assignment_service.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(AssignmentNotFoundException.class)
    public ResponseEntity<String> handleAssignmentException(AssignmentNotFoundException ex){
        return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(ex.getMessage());
    }
    
    @ExceptionHandler(DuplicateAssignmentException.class)
    public ResponseEntity<String> handleDuplicateAssignmentException(DuplicateAssignmentException ex){
        return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(ex.getMessage());
    }
    
    @ExceptionHandler(ServiceUnavailableException.class)
	public ResponseEntity<String> handleServiceUnavailableException(ServiceUnavailableException e) {
		return ResponseEntity.status(HttpStatus.SC_SERVICE_UNAVAILABLE).body(e.getMessage());
	}
    
    @ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleOtherException(Exception e) {
		return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
	}
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,String>> handleValidationException(MethodArgumentNotValidException ex) {
	    Map<String,String> errors = new HashMap<>();
	    ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
	   
	    return ResponseEntity.badRequest().body(errors);
	}
}
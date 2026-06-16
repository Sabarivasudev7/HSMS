package cap_stone_project.booking_service.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(BookingNotFoundException.class)
	public ResponseEntity<String> handleBookingNotFoundException(BookingNotFoundException e) {
		return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(e.getMessage());
	}
	
	
	
	@ExceptionHandler(InvalidBookingStateException.class)
	public ResponseEntity<String> handleInvalidBookingStateException(InvalidBookingStateException e) {
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(e.getMessage());
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

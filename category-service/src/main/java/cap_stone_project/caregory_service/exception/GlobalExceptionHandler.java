package cap_stone_project.caregory_service.exception;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler{
	
	
	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException e) {
		
		return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(e.getMessage());
	}
	
	
	@ExceptionHandler(DuplicateCategoryException.class)
	public ResponseEntity<String> handleDuplicateCategoryException(DuplicateCategoryException e) {
		
		return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(e.getMessage());
	}
	
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleOtherException(Exception e) {
		
		return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

}

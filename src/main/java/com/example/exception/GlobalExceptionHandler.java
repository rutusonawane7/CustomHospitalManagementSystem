package com.example.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.entity.CustomError;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UsernameAlreadyExistsException.class)
	public ResponseEntity<CustomError> handleUsernameExists(UsernameAlreadyExistsException ex)
	{
		CustomError error = new CustomError(
				LocalDateTime.now(), 
				HttpStatus.CONFLICT.value(), 
				ex.getMessage());
		
		return new ResponseEntity<>(error,HttpStatus.CONFLICT);
				
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CustomError> handleValidation(MethodArgumentNotValidException ex){
		
		String message = ex.getBindingResult()
				.getFieldErrors()
				.get(0)
				.getDefaultMessage();
		
		CustomError error = new CustomError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), message);
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<CustomError> handleUsernameNotFound() {
	    return new ResponseEntity<>(
	        new CustomError(LocalDateTime.now(), 401, "Username does not exist"),
	        HttpStatus.UNAUTHORIZED
	    );
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<CustomError> handleBadCredentials(BadCredentialsException ex) {

	    CustomError error = new CustomError(
	            LocalDateTime.now(),
	            HttpStatus.UNAUTHORIZED.value(), // 401
	            "Invalid username or password"
	    );

	    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<CustomError> handleResourceNotFound(ResourceNotFoundException ex){
		CustomError error = new CustomError(LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	
	
	 @ExceptionHandler(TokenExpiredException.class)
	    public ResponseEntity<Map<String, String>> handleExpired(TokenExpiredException ex) {
	        Map<String, String> body = new HashMap<>();
	        body.put("code", "401");
	        body.put("message", ex.getMessage());
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
	    }

}



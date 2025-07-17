package com.springboot.bankDemo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springboot.bankDemo.exception.InvalidInputException;
import com.springboot.bankDemo.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	// for Unforeseen Exception Handling
	@ExceptionHandler(exception = Exception.class)
	public ResponseEntity<?> handleException(Exception e) {
		Map<String, String> map = new HashMap<>();
		map.put("msg", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
	}

	// for Runtime Exception Handling
	@ExceptionHandler(exception = RuntimeException.class)
	public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
		Map<String, String> map = new HashMap<>();
		map.put("msg", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
	}

	// for Resource not found Exception Handling
	@ExceptionHandler(exception = ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
		Map<String, String> map = new HashMap<>();
		map.put("msg", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
	}
	
	// for invalid input exception
	@ExceptionHandler(exception = InvalidInputException.class)
	public ResponseEntity<?> handleInputInvalidException(InvalidInputException e) {
		Map<String, String> map = new HashMap<>();
		map.put("msg", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
	}
}

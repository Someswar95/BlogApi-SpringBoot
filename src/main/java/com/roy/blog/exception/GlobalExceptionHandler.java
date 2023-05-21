package com.roy.blog.exception;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.roy.blog.playloads.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFountException(ResourceNotFoundException ex) {
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> methodArgsNotValidException(MethodArgumentNotValidException ex) {
		Map<String, String> respMap = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			respMap.put(fieldName, message);
		});

		return new ResponseEntity<Map<String, String>>(respMap, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse> handleApiException(ApiException ex) {
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, true);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<String> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
		return new ResponseEntity<String>("Request method not supported", HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResponse> userNotFountException(UserNotFoundException ex) {
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<String> nullPointerException(NullPointerException ex) {
		return new ResponseEntity<String>("404 not found !!", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<String> signatureException(SignatureException ex) {
		return new ResponseEntity<String>(
				"JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}

package com.steerify.exceptions;

import com.steerify.ErrorResponse.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEntityException.class)
    public static ResponseEntity<ErrorResponse> handleDuplicateEntityException(DuplicateEntityException message) {
        ErrorResponse errorResponse = new ErrorResponse(message.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(JobNotFoundException.class)
    public static ResponseEntity<ErrorResponse> handleJobNotFoundException(JobNotFoundException errpr) {
        ErrorResponse errorResponse = new ErrorResponse(errpr.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler({RuntimeException.class})
    public static ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException error) {
        ErrorResponse errorResponse = new ErrorResponse("An error occurred: " + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(ExpiredJwtException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                "Token expired...pls login",
                HttpStatus.UNAUTHORIZED.value()
        );
        return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler({ResourceNotFoundException.class})
    public static ResponseEntity<ErrorResponse> handleRuntimeException(ResourceNotFoundException error) {
        ErrorResponse errorResponse = new ErrorResponse("An error occurred: " + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

    }


        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleValidationExceptions(
                MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        }


}


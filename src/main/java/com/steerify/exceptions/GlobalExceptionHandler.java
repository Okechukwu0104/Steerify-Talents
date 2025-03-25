package com.steerify.exceptions;

import com.steerify.ErrorResponse.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(RuntimeException.class)
    public static ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException error) {
        ErrorResponse errorResponse = new ErrorResponse("An error occurred: " + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }



}


package com.tms.tms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        @ExceptionHandler(SecurityException.class)
        public ResponseEntity<ErrorResponse> handleSecurityException(SecurityException ex) {
                log.error("Forbidden: {}", ex.getMessage());

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .error(ErrorResponse.ErrorDetail.builder()
                                                .code("FORBIDDEN")
                                                .message(ex.getMessage())
                                                .build())
                                .build();

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
                log.error("Entity not found: {}", ex.getMessage());

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .error(ErrorResponse.ErrorDetail.builder()
                                                .code("ENTITY_NOT_FOUND")
                                                .message(ex.getMessage())
                                                .build())
                                .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
                log.error("Validation error: {}", ex.getMessage());

                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getAllErrors().forEach((error) -> {
                        String fieldName = ((FieldError) error).getField();
                        String errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                });

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .error(ErrorResponse.ErrorDetail.builder()
                                                .code("VALIDATION_ERROR")
                                                .message("Invalid input data")
                                                .details(errors)
                                                .build())
                                .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
                log.error("Response status exception: {}", ex.getMessage());

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .error(ErrorResponse.ErrorDetail.builder()
                                                .code("BAD_REQUEST")
                                                .message(ex.getReason())
                                                .build())
                                .build();

                return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
        }

        @ExceptionHandler({ AccessDeniedException.class, InsufficientAuthenticationException.class })
        public ResponseEntity<ErrorResponse> handleAccessDenied(Exception ex) {
                log.error("Access denied: {}", ex.getMessage());

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .error(ErrorResponse.ErrorDetail.builder()
                                                .code("ACCESS_DENIED")
                                                .message("Access denied")
                                                .build())
                                .build();

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
                log.error("Unexpected error: ", ex);

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .error(ErrorResponse.ErrorDetail.builder()
                                                .code("INTERNAL_SERVER_ERROR")
                                                .message("An unexpected error occurred")
                                                .build())
                                .build();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
}

package com.vuthy.mobilebankingapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestControllerAdvice
public class ServiceException {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleServiceException(ResponseStatusException e) {
        ErrorResponse<String> error = ErrorResponse.<String>builder()
                .message("Business logic error")
                .status(e.getStatusCode().value())
                .timestamp(LocalDate.now())
                .details(e.getReason())
                .build();

        return ResponseEntity
                .status(e.getStatusCode().value())
                .body(error);

    }
}

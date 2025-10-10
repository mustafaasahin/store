package com.spring.store.exception;

import com.spring.store.dto.common.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpBadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(HttpBadRequestException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex, request, false);
    }

    @ExceptionHandler(HttpMethodNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpMethodNotAllowedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, ex, request, false);
    }

    @ExceptionHandler(HttpNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(HttpNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex, request, false);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandled(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request, true);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, Exception ex, HttpServletRequest request, boolean errorLevel) {
        if (errorLevel) {
            log.error("{}: {}", status, ex.getMessage(), ex);
        } else {
            log.warn("{}: {}", status, ex.getMessage());
        }

        String message = ex.getMessage() != null ? ex.getMessage() : status.getReasonPhrase();
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }
}

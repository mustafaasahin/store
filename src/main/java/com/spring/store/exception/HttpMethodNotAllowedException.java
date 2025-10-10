package com.spring.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class HttpMethodNotAllowedException extends RuntimeException {

    public HttpMethodNotAllowedException(String message) {
        super(message);
    }
}

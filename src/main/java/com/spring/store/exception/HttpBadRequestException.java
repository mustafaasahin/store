package com.spring.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HttpBadRequestException extends RuntimeException {

    public HttpBadRequestException(String message) {
        super(message);
    }
}

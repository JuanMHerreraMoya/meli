package com.meli.app.application.port.out.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

public interface ErrorHandlerPort {
    ResponseEntity<ErrorResponse> handle(Exception e);
}

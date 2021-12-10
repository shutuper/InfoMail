package com.infopulse.infomail.configuration;

import com.infopulse.infomail.exceptions.EmailScheduleException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = EmailScheduleException.class)
    protected ResponseEntity<?> EmailScheduleExceptionHandler(RuntimeException ex) {
        return ResponseEntity.badRequest().build();
    }

}

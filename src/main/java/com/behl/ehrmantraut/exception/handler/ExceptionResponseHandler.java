package com.behl.ehrmantraut.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.behl.ehrmantraut.exception.dto.ExceptionResponseDto;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> serverExceptionHandler(Exception exception) {
        log.error("Exception Caught:", exception);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(new ExceptionResponseDto("Something went wrong. Contact your administrator"));
    }

}

package com.behl.ehrmantraut.exception.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.behl.ehrmantraut.exception.CodeChallengeAndVerifierMismatchException;
import com.behl.ehrmantraut.exception.DuplicateEmailIdException;
import com.behl.ehrmantraut.exception.GenericBadRequestException;
import com.behl.ehrmantraut.exception.GenericUnauthorizedExcpetion;
import com.behl.ehrmantraut.exception.InvalidCodeException;
import com.behl.ehrmantraut.exception.InvalidCredentialsException;
import com.behl.ehrmantraut.exception.dto.ExceptionResponseDto;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    @ExceptionHandler(DuplicateEmailIdException.class)
    public ResponseEntity<?> duplicateEmailIdExceptionHandler(final DuplicateEmailIdException exception) {
        log.error("Exception Caught:", exception);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ExceptionResponseDto("User account already exists with provided email-id"));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(GenericBadRequestException.class)
    public ResponseEntity<?> genericBadRequestExceptionHandler(final GenericBadRequestException exception) {
        log.error("Exception Caught:", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponseDto(exception.getMessage()));
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> invalidCredentialsExceptionHandler(final InvalidCredentialsException exception) {
        log.error("Exception Caught:", exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponseDto("Invalid login credentials!"));
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<?> invalidCodeExceptionHandler(final InvalidCodeException exception) {
        log.error("Exception Caught:", exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponseDto("Code provided is either invalid or used"));
    }

    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    @ExceptionHandler(CodeChallengeAndVerifierMismatchException.class)
    public ResponseEntity<?> codeChallengeAndVerifierMismatchExceptionHandler(
            final CodeChallengeAndVerifierMismatchException exception) {
        log.error("Exception Caught:", exception);
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .body(new ExceptionResponseDto("Code verifier does not correspond to earlier provided code challenge"));
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(GenericUnauthorizedExcpetion.class)
    public ResponseEntity<?> genericUnauthorizedExcpetionHandler(final GenericUnauthorizedExcpetion exception) {
        log.error("Exception Caught:", exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponseDto("You are unauthorized to perform this action"));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        final var response = new HashMap<>();
        response.put("status", "Failure");
        response.put("message",
                fieldErrors.stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList()));
        response.put("timestamp",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
        return ResponseEntity.badRequest().body(response.toString());
    }

    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> serverExceptionHandler(final Exception exception) {
        log.error("Exception Caught:", exception);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(new ExceptionResponseDto("Something went wrong. Contact your administrator"));
    }

}

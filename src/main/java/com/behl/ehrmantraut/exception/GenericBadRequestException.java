package com.behl.ehrmantraut.exception;

public class GenericBadRequestException extends RuntimeException {

    private static final long serialVersionUID = 6629564389480900422L;

    public GenericBadRequestException() {
        super();
    }

    public GenericBadRequestException(String message) {
        super(message);
    }

}

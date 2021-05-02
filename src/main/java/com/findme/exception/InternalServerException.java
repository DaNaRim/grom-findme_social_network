package com.findme.exception;

public class InternalServerException extends RuntimeException {

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

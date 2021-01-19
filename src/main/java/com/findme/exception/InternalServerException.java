package com.findme.exception;

public class InternalServerException extends Exception {

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

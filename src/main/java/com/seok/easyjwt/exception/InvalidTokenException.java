package com.seok.easyjwt.exception;

public class InvalidTokenException extends EasyJwtException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
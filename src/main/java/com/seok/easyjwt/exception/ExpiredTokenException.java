package com.seok.easyjwt.exception;

public class ExpiredTokenException extends EasyJwtException {

    public ExpiredTokenException(String message) {
        super(message);
    }

    public ExpiredTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.seok.easyjwt.exception;

public class EasyJwtException extends RuntimeException {

    public EasyJwtException(String message) {
        super(message);
    }

    public EasyJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.dtalk.ecosystem.exceptions;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message){
        super(message);
    }
}

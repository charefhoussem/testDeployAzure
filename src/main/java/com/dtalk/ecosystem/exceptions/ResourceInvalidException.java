package com.dtalk.ecosystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceInvalidException extends  RuntimeException{

    public ResourceInvalidException(String message){
        super(message);
    }
}

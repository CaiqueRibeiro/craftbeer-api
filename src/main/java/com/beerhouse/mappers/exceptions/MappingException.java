package com.beerhouse.mappers.exceptions;

public class MappingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MappingException(String msg) {
        super(msg);
    }

    public MappingException(String msg, Throwable cause) {
        super(msg, cause);
    }

}

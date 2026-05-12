package com.example.Libary_backend.exception;

public class BookAlreadyIssuedException extends RuntimeException {
    
    public BookAlreadyIssuedException(String message) {
        super(message);
    }
    
    public BookAlreadyIssuedException(String message, Throwable cause) {
        super(message, cause);
    }
}

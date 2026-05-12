package com.example.Libary_backend.exception;

public class MemberMaxBooksExceededException extends RuntimeException {
    
    public MemberMaxBooksExceededException(String message) {
        super(message);
    }
    
    public MemberMaxBooksExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}

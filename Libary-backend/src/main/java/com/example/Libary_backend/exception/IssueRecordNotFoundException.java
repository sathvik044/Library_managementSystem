package com.example.Libary_backend.exception;

public class IssueRecordNotFoundException extends RuntimeException {
    
    public IssueRecordNotFoundException(String message) {
        super(message);
    }
    
    public IssueRecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

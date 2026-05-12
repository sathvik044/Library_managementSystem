package com.example.Libary_backend.exception;

public class InvalidSearchKeywordException extends RuntimeException {

    public InvalidSearchKeywordException(String message) {
        super(message);
    }
}
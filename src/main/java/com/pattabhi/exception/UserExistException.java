package com.pattabhi.exception;

public class UserExistException extends Throwable {

    public UserExistException(String message) {
        super(message);

    }

    public UserExistException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

package com.pattabhi.exception;

public class UserAlreadyExistException extends Throwable {

    public UserAlreadyExistException(String message) {
        super(message);

    }

    public UserAlreadyExistException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

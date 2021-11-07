package io.gitlab.hsedjame.buzz.services.exceptions;

import org.springframework.http.HttpStatus;

public sealed class BuzzException extends Exception {
    private final String message;
    private final HttpStatus status;

    public BuzzException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage(){
        return message;
    }

    public HttpStatus getStatus(){
        return status;
    }

    public static final class NameAlreadyUsed extends BuzzException{
        public NameAlreadyUsed(String name) {
            super(String.format("Name %s already used. Please choose another.", name), HttpStatus.NOT_ACCEPTABLE);
        }
    }

}

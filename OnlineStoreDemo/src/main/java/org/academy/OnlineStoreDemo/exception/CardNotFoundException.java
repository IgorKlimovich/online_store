package org.academy.OnlineStoreDemo.exception;

public class CardNotFoundException extends RuntimeException{

    public CardNotFoundException() {
    }

    public CardNotFoundException(String message) {
        super(message);
    }

    public CardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

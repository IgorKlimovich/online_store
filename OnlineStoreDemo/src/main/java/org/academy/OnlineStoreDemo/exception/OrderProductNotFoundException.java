package org.academy.OnlineStoreDemo.exception;

public class OrderProductNotFoundException extends RuntimeException{

    public OrderProductNotFoundException() {
    }

    public OrderProductNotFoundException(String message) {
        super(message);
    }

    public OrderProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

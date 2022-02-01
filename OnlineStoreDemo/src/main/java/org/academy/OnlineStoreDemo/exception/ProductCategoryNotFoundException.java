package org.academy.OnlineStoreDemo.exception;

public class ProductCategoryNotFoundException extends RuntimeException{
    public ProductCategoryNotFoundException() {
    }

    public ProductCategoryNotFoundException(String message) {
        super(message);
    }

    public ProductCategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

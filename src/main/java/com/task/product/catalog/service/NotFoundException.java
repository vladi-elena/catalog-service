package com.task.product.catalog.service;

public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {
        super(message);
    }
}

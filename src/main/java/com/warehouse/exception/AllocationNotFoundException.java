package com.warehouse.exception;

public class AllocationNotFoundException extends RuntimeException {

    public AllocationNotFoundException(String message) {
        super(message);
    }

}
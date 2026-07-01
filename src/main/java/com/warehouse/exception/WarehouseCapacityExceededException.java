package com.warehouse.exception;

public class WarehouseCapacityExceededException extends RuntimeException {

    public WarehouseCapacityExceededException(String message) {
        super(message);
    }

}
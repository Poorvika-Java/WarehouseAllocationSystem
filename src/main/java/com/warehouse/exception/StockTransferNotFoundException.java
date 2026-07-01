package com.warehouse.exception;

public class StockTransferNotFoundException extends ResourceNotFoundException {

    public StockTransferNotFoundException(String message) {
        super(message);
    }

}
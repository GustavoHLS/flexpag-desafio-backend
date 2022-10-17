package com.flexpag.exception;

public class IdNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "Transaction not found.";
    }
}

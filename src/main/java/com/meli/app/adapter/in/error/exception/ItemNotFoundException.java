package com.meli.app.adapter.in.error.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String title) {
        super("Item: " + title + " no encontrado");
    }
}
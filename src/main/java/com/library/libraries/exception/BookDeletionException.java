package com.library.libraries.exception;

public class BookDeletionException extends RuntimeException {

    public BookDeletionException(String message) {
        super(message);
    }

    public BookDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.library.libraries.exception;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException() {
        super("Book already exists");
    }

}

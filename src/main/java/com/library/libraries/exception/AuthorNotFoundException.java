package com.library.libraries.exception;

public class AuthorNotFoundException extends RuntimeException {

    public AuthorNotFoundException(Long id) {
        super("Author does not exist: " + id);
    }

    public AuthorNotFoundException() {
        super("Author does not exist");
    }
}

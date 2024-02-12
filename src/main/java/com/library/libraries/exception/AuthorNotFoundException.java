package com.library.libraries.exception;

public class AuthorNotFoundException extends RuntimeException {

    public AuthorNotFoundException() {
        super("Author does not exist");
    }
}

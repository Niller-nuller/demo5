package org.example.demo5.g_Exceptions;

public class PasswordException extends RuntimeException {
    public PasswordException(String message) {
        super(message);
    }
}

package org.example.util;

public class MailInvalidException extends RuntimeException {
    public MailInvalidException(String message) {
        super(message);
    }
}

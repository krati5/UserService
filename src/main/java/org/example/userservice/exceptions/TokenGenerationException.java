package org.example.userservice.exceptions;

public class TokenGenerationException extends RuntimeException{
    public TokenGenerationException(String message) {
        super(message);
    }
}

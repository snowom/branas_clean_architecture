package com.curso.cleancode.branas.exceptions;

public class PassengerNotFoundException extends RuntimeException{
    public PassengerNotFoundException(String message) {
        super(message);
    }
}

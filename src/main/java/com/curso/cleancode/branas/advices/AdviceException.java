package com.curso.cleancode.branas.advices;

import com.curso.cleancode.branas.exceptions.RideException;
import com.curso.cleancode.branas.exceptions.RideNotFoundException;
import com.curso.cleancode.branas.exceptions.UserException;
import com.curso.cleancode.branas.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AdviceException {

    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private Map<String, String> passengerException(UserException userException) {
        Map<String, String> response = new HashMap<>();
        response.put("message", userException.getMessage());
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private Map<String, String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = ((FieldError) error).getDefaultMessage();
            errors.put("message", errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private Map<String, String> passengerNotFoundException(UserNotFoundException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return response;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private Map<String, String> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return response;
    }

    @ExceptionHandler(RideException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private Map<String, String> rideException(RideException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return response;
    }

    @ExceptionHandler(RideNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private Map<String, String> rideNotFoundException(RideNotFoundException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return response;
    }
}

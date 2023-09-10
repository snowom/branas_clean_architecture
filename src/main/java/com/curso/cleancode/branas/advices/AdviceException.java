package com.curso.cleancode.branas.advices;

import com.curso.cleancode.branas.exceptions.PassengerException;
import com.curso.cleancode.branas.exceptions.PassengerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AdviceException {

    @ExceptionHandler(PassengerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private Map<String, String> passengerException(PassengerException passengerException) {
        Map<String, String> response = new HashMap<>();
        response.put("message", passengerException.getMessage());
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

    @ExceptionHandler(PassengerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private Map<String, String> passengerNotFoundException(PassengerNotFoundException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return response;
    }
}

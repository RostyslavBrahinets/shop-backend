package com.shop.errors;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ExceptionAdvice {
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleNotFoundException(
        NotFoundException e
    ) {
        String message = e.getMessage();
//        logger.error(getClass().getSimpleName(), message, e);
        return new ErrorDto(message);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorDto handleValidationException(
        ValidationException e
    ) {
        String message = e.getMessage();
//        logger.error(getClass().getSimpleName(), message, e);
        return new ErrorDto(message);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorDto handleIllegalArgumentException(
        IllegalArgumentException e
    ) {
        String message = e.getMessage();
//        logger.error(getClass().getSimpleName(), message, e);
        return new ErrorDto(message);
    }
}

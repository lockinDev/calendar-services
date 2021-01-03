package com.calendar.service.util.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.calendar.service.util.exceptions.InvalidInputException;
import com.calendar.service.util.exceptions.NotFoundException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = NOT_FOUND)    
    public @ResponseBody
    HttpErrorInfo handleNotFoundExceptions(WebRequest request, Exception ex) {
 
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }

    
    @ExceptionHandler(value = InvalidInputException.class)
    @ResponseStatus(value = UNPROCESSABLE_ENTITY)
    public @ResponseBody HttpErrorInfo handleInvalidInputException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, WebRequest request, Exception ex) {
        final String path = request.getContextPath();
        final String message = ex.getMessage();

        LOG.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);
        return new HttpErrorInfo(httpStatus, path, message);
    }
}
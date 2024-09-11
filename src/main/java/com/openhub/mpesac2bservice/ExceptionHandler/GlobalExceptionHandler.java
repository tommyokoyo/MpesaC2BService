package com.openhub.mpesac2bservice.ExceptionHandler;

import com.openhub.mpesac2bservice.Models.ResponseUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException ex) {
        System.out.println( "HTTP Resource Not Found: " + ex.getMessage());
        return ResponseUtil.buildResponse(
                HttpStatus.BAD_REQUEST,
                "Failed",
                "Resource Not Found");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getCause() + " Payload");
        System.out.println( "HTTP message not readable: " + ex.getMessage());
        return ResponseUtil.buildResponse(
                HttpStatus.BAD_REQUEST,
                "Error",
                "HTTP message not readable");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
        System.out.println( "HTTP media type not supported: " + ex.getMessage());
        return ResponseUtil.buildResponse(
                HttpStatus.BAD_REQUEST,
                "Error",
                "HTTP media type not supported");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + ex.getMessage());
        System.out.println( "HTTP method not supported: " + ex.getMessage());
        return ResponseUtil.buildResponse(
                HttpStatus.BAD_REQUEST,
                "Error",
                "Request method not supported");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error executing request");
        System.out.println( "Data Integrity Error: " + ex.getMessage());
        return ResponseUtil.buildResponse(
                HttpStatus.BAD_REQUEST,
                "Error",
                "Could not execute request");
    }
}

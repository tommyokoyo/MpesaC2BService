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
        return ResponseUtil.buildMpesaErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Resource Not Found Exception",
                "HTTP Resource Not Found");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        System.out.println( "HTTP message not readable: " + ex.getMessage());
        return ResponseUtil.buildMpesaErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Message not readable Exception",
                "HTTP message not readable");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        System.out.println( "HTTP media type not supported: " + ex.getMessage());
        return ResponseUtil.buildMpesaErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Media Type NotSupported Exception",
                "HTTP media type not supported");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        System.out.println( "HTTP method not supported: " + ex.getMessage());
        return ResponseUtil.buildMpesaErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Method Not Supported Exception",
                "Request method not supported");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        System.out.println( "Data Integrity Error: " + ex.getMessage());
        return ResponseUtil.buildMpesaErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Data Integrity Exception",
                "Could not execute request");
    }
}

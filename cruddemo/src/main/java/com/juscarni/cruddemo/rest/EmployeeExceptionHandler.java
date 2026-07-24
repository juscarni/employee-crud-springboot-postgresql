package com.juscarni.cruddemo.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EmployeeExceptionHandler {
    
  @ExceptionHandler
    public ResponseEntity<EmployeeErrorResponse> handleException(EmployeeNotFoundException ex){
        EmployeeErrorResponse studenteErrorResponse = new EmployeeErrorResponse();

        studenteErrorResponse.setMessage(ex.getMessage());
        studenteErrorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        studenteErrorResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(studenteErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorResponse> handleException(Exception ex){
        EmployeeErrorResponse studenteErrorResponse = new EmployeeErrorResponse();

        studenteErrorResponse.setMessage(ex.getMessage());
        studenteErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        studenteErrorResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(studenteErrorResponse, HttpStatus.NOT_FOUND);
    }
}

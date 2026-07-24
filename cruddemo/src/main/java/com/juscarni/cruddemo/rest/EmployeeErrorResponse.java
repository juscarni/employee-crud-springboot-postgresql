package com.juscarni.cruddemo.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeErrorResponse{
    private String message;
    private long timestamp;
    private int status;
}
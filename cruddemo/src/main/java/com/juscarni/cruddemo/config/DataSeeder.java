package com.juscarni.cruddemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.juscarni.cruddemo.service.EmployeeService;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EmployeeService employeeService;

    @Autowired
    public DataSeeder(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void run(String... args) {
        employeeService.createEmployeeWithRole("John", "Doe", "john.doe@gmail.com", "JohnDoe#2026", "ROLE_USER");
        employeeService.createEmployeeWithRole("Jane", "Smith", "jane.smith@gmail.com", "JaneSmith#2026", "ROLE_MANAGER");
        employeeService.createEmployeeWithRole("Michael", "Johnson", "michael.johnson@gmail.com", "MichaelJ#2026", "ROLE_ADMIN");
        employeeService.createEmployeeWithRole("Emily", "Brown", "emily.brown@gmail.com", "EmilyBrown#2026", "ROLE_USER");
        employeeService.createEmployeeWithRole("David", "Wilson", "david.wilson@gmail.com", "DavidW#2026", "ROLE_MANAGER");
        employeeService.createEmployeeWithRole("Sarah", "Taylor", "sarah.taylor@gmail.com", "SarahT#2026", "ROLE_ADMIN");
        employeeService.createEmployeeWithRole("Daniel", "Anderson", "daniel.anderson@gmail.com", "DanielA#2026", "ROLE_USER");
        employeeService.createEmployeeWithRole("Laura", "Thomas", "laura.thomas@gmail.com", "LauraT#2026", "ROLE_MANAGER");
        employeeService.createEmployeeWithRole("James", "Jackson", "james.jackson@gmail.com", "JamesJ#2026", "ROLE_ADMIN");
    }
}
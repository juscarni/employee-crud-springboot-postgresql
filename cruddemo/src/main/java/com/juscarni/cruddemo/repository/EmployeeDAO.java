package com.juscarni.cruddemo.repository;

import java.util.List;

import com.juscarni.cruddemo.entity.Employee;

public interface EmployeeDAO {

    //create
    void save(Employee employee);

    //read
    List<Employee> findAll();
    Employee findById(int id);
    Employee findByEmail(String email);

    //update
    Employee update(Employee employee);

    //delete
    void deleteById(int id);
} 

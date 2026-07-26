package com.juscarni.cruddemo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.juscarni.cruddemo.entity.Employee;
import com.juscarni.cruddemo.entity.Roles;
import com.juscarni.cruddemo.repository.EmployeeDAO;
import com.juscarni.cruddemo.repository.RolesDAO;
import com.juscarni.cruddemo.rest.EmployeeNotFoundException;

import jakarta.transaction.Transactional;
import tools.jackson.databind.json.JsonMapper;

@Service
public class EmployeeService {
    
    private EmployeeDAO employeeDAO;
    private JsonMapper jsonMapper;
    private RolesDAO rolesDAO;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeDAO employeeDAO, JsonMapper jsonMapper , RolesDAO rolesDAO, PasswordEncoder passwordEncoder){
        this.employeeDAO = employeeDAO;
        this.jsonMapper = jsonMapper;
        this.rolesDAO = rolesDAO;
        this.passwordEncoder = passwordEncoder;
    }


    /*@PostConstruct
    public void init() {
        createEmployeeWithRole("John", "Doe", "john.doe@gmail.com", "ROLE_USER");
        createEmployeeWithRole("Jane", "Smith", "jane.smith@gmail.com", "ROLE_MANAGER");
        createEmployeeWithRole("Michael", "Johnson", "michael.johnson@gmail.com", "ROLE_ADMIN");
        createEmployeeWithRole("Emily", "Brown", "emily.brown@gmail.com", "ROLE_USER");
        createEmployeeWithRole("David", "Wilson", "david.wilson@gmail.com", "ROLE_MANAGER");
        createEmployeeWithRole("Sarah", "Taylor", "sarah.taylor@gmail.com", "ROLE_ADMIN");
        createEmployeeWithRole("Daniel", "Anderson", "daniel.anderson@gmail.com", "ROLE_USER");
        createEmployeeWithRole("Laura", "Thomas", "laura.thomas@gmail.com", "ROLE_MANAGER");
        createEmployeeWithRole("James", "Jackson", "james.jackson@gmail.com", "ROLE_ADMIN");
    }*/
    
    @Transactional
    public void createEmployeeWithRole(String firstName, String lastName, String email, String password,  String roleName) {
       Employee employee = new Employee(firstName, lastName, email, password);
       employee.setPassword(this.passwordEncoder.encode(password)); 

        Roles role = rolesDAO.findByRole(roleName); // cherche en base d'abord
        if (role == null) {
            role = rolesDAO.save(new Roles().setRole(roleName)); // crée seulement si absent
        }
        employee.getRoles().add(role);
        createEmployee(employee);
    }


    public void createEmployee(Employee employee){
        this.employeeDAO.save(employee);
    }

    public Employee updateEmployee(int id , Map<String, Object> payload){
        if(payload.containsKey("id")){
            throw new IllegalArgumentException("you can not modify an id, take the id out of the payload body.");
        }
        Employee tempEmployee = getEmployeeById(id);
        Employee employee = this.jsonMapper.updateValue(tempEmployee, payload);

        if(payload.containsKey("password")){
            employee.setPassword(passwordEncoder.encode(String.valueOf(payload.get("password"))));
        }

        return this.employeeDAO.update(employee);
    }

    public List<Employee> getAllEmployees(){
        List<Employee> employees = this.employeeDAO.findAll();
        return employees;
    }

    public Employee getEmployeeById(int id){
        Employee employee = this.employeeDAO.findById(id);
        if(employee == null){
            throw new EmployeeNotFoundException("there is not an employee in the database with the id : " + id);
        }
        return employee;
    }

    public Employee getEmployeeByEmail(String email){
        Employee employee = this.employeeDAO.findByEmail(email);
         if(employee == null){
            throw new EmployeeNotFoundException("there is not an employee with the email : "+ email);
        }
        return employee;
    }

    public String deleteEmployeeById(int id){
        Employee employee = getEmployeeById(id);
        this.employeeDAO.deleteById(employee.getId());
        return "the employee with id : " + id + " was deleted";
    }
}

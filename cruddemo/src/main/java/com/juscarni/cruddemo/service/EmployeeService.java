package com.juscarni.cruddemo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.juscarni.cruddemo.entity.Employee;
import com.juscarni.cruddemo.repository.EmployeeDAO;
import com.juscarni.cruddemo.rest.EmployeeNotFoundException;

import jakarta.annotation.PostConstruct;
import tools.jackson.databind.json.JsonMapper;

@Service
public class EmployeeService {
    
    private EmployeeDAO employeeDAO;
    private JsonMapper jsonMapper;

    @Autowired
    public EmployeeService(EmployeeDAO employeeDAO, JsonMapper jsonMapper){
        this.employeeDAO = employeeDAO;
        this.jsonMapper = jsonMapper;
    }


    @PostConstruct
    private void init() {
        createEmployee(new Employee("John", "Doe", "john.doe@gmail.com"));
        createEmployee(new Employee("Jane", "Smith", "jane.smith@gmail.com"));
        createEmployee(new Employee("Michael", "Johnson", "michael.johnson@gmail.com"));
        createEmployee(new Employee("Emily", "Brown", "emily.brown@gmail.com"));
        createEmployee(new Employee("David", "Wilson", "david.wilson@gmail.com"));
        createEmployee(new Employee("Sarah", "Taylor", "sarah.taylor@gmail.com"));
        createEmployee(new Employee("Daniel", "Anderson", "daniel.anderson@gmail.com"));
        createEmployee(new Employee("Laura", "Thomas", "laura.thomas@gmail.com"));
        createEmployee(new Employee("James", "Jackson", "james.jackson@gmail.com"));
        createEmployee(new Employee("Sophia", "White", "sophia.white@gmail.com"));
        createEmployee(new Employee("William", "Harris", "william.harris@gmail.com"));
        createEmployee(new Employee("Olivia", "Martin", "olivia.martin@gmail.com"));
        createEmployee(new Employee("Benjamin", "Thompson", "benjamin.thompson@gmail.com"));
        createEmployee(new Employee("Emma", "Garcia", "emma.garcia@gmail.com"));
        createEmployee(new Employee("Lucas", "Martinez", "lucas.martinez@gmail.com"));
        createEmployee(new Employee("Mia", "Robinson", "mia.robinson@gmail.com"));
        createEmployee(new Employee("Alexander", "Clark", "alexander.clark@gmail.com"));
        createEmployee(new Employee("Charlotte", "Rodriguez", "charlotte.rodriguez@gmail.com"));
        createEmployee(new Employee("Henry", "Lewis", "henry.lewis@gmail.com"));
        createEmployee(new Employee("Amelia", "Walker", "amelia.walker@gmail.com"));
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

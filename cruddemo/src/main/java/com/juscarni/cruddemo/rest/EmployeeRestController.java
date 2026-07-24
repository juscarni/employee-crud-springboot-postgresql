package com.juscarni.cruddemo.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.juscarni.cruddemo.entity.Employee;
import com.juscarni.cruddemo.service.EmployeeService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeRestController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }
    
    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return this.employeeService.getAllEmployees();
    }
    
    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable int id){
        return this.employeeService.getEmployeeById(id);
    }

    @GetMapping("/employees/email/{email}")
    public Employee getEmployee(@PathVariable String email){
        return this.employeeService.getEmployeeByEmail(email);
    }

    @PostMapping("/employees")
    public String createEmployee(@RequestBody Employee employee){
        this.employeeService.createEmployee(employee);
        return "the employee was saved successfully in the database";
    }

    @DeleteMapping("/employees/{id}")
    public String deleteById(@PathVariable int id){
        return this.employeeService.deleteEmployeeById(id);
    }

    @PatchMapping("/employees/{id}")
    public Employee update(@PathVariable int id, @RequestBody Map<String, Object> payload){
        return this.employeeService.updateEmployee(id, payload);
    }

}

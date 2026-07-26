package com.juscarni.cruddemo.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.juscarni.cruddemo.entity.Employee;
import com.juscarni.cruddemo.entity.Role;
import com.juscarni.cruddemo.repository.EmployeeDAO;

@Service
public class EmployeeUserDetailsService implements UserDetailsService{
    private final EmployeeDAO employeeDAO;

    public EmployeeUserDetailsService(EmployeeDAO employeeDAO){
        this.employeeDAO = employeeDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Employee employee = this.employeeDAO.findByEmail(username);
       if(employee == null){
            throw new UsernameNotFoundException("Employee not found with email : " + username);
       }
       return User.builder()
                .username(employee.getEmail())
                .password(employee.getPassword())
                .authorities(employee.getRoles().stream().map(Role::getRole).toArray(String[]::new)) // with authorities if we have ROLE_... in the database , roles if in the database we just have MANAGER o ADMIN and ROLE_ will be added automatically
                .build();
    }
}

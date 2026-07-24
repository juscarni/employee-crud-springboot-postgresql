package com.juscarni.cruddemo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.juscarni.cruddemo.entity.Employee;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO{

    private  EntityManager entityManager;

    @Autowired
    public EmployeeDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<Employee> findAll() {
        TypedQuery<Employee> typedQuery = this.entityManager.createQuery(" FROM Employee", Employee.class);
        return typedQuery.getResultList();
    }

    @Override
    public Employee findById(int id) {
        Employee employee = this.entityManager.find(Employee.class, id);
        return employee;
    }

    @Override
    public Employee findByEmail(String email) {
      TypedQuery<Employee> typedQuery = this.entityManager.createQuery("FROM Employee where email=:theData", Employee.class);
      typedQuery.setParameter("theData", email);
      
      return typedQuery.getSingleResultOrNull();
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        this.entityManager.remove(findById(id));
    }

    @Override
    @Transactional
    public void save(Employee employee) {
      this.entityManager.persist(employee);
    }

    @Override
    @Transactional
    public Employee update(Employee employee) {
       Employee dbEmployee = this.entityManager.merge(employee);
       return dbEmployee;
    }
    
}

package com.juscarni.cruddemo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.juscarni.cruddemo.entity.Roles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class RolesDAOImpl implements RolesDAO{
    
    private EntityManager entityManager;

    @Autowired
    public RolesDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Roles save(Roles role) {
      this.entityManager.persist(role);
      return role;
    }

    @Override
    public Roles findByRole(String role) {
        TypedQuery<Roles> query = entityManager.createQuery(
            "FROM Roles WHERE role = :theRole", Roles.class);
        query.setParameter("theRole", role);
        
        List<Roles> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}

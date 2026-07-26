package com.juscarni.cruddemo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.juscarni.cruddemo.entity.Role;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class RoleDAOImpl implements RolesDAO{
    
    private EntityManager entityManager;

    @Autowired
    public RoleDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Role save(Role role) {
      this.entityManager.persist(role);
      return role;
    }

    @Override
    public Role findByRole(String role) {
        TypedQuery<Role> query = entityManager.createQuery(
            "FROM Role WHERE role = :theRole", Role.class);
        query.setParameter("theRole", role);
        
        List<Role> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}

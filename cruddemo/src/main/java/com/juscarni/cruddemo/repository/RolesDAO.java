package com.juscarni.cruddemo.repository;

import com.juscarni.cruddemo.entity.Roles;

public interface RolesDAO {
    Roles save(Roles role);
    Roles findByRole(String role);
}

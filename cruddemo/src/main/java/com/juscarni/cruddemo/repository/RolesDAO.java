package com.juscarni.cruddemo.repository;

import com.juscarni.cruddemo.entity.Role;

public interface RolesDAO {
    Role save(Role role);
    Role findByRole(String role);
}

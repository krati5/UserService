package org.example.userservice.services;

import org.example.userservice.exceptions.NotFoundException;
import org.example.userservice.models.Role;

import java.util.List;

public interface IRoleService {
    Role createRole(Role role);

    Role updateRole(Long id, Role roleDetails) throws NotFoundException;

    void deleteRole(Long id) throws NotFoundException;

    Role getRoleById(Long id) throws NotFoundException;

    List<Role> getAllRoles();
}

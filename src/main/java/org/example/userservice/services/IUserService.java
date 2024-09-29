package org.example.userservice.services;

import org.example.userservice.dtos.UserDTO;
import org.example.userservice.exceptions.NotFoundException;
import org.example.userservice.models.User;

import java.util.List;

public interface IUserService {
    // Get user by ID
    User getUser(Long id) throws NotFoundException;

    // Update user by ID
    User updateUser(Long id, UserDTO userDTO) throws NotFoundException;

    // Delete user by ID
    void deleteUser(Long id) throws NotFoundException;

    // List all users
    List<User> listUsers();
}

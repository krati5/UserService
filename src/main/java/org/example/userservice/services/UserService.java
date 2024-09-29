package org.example.userservice.services;

import lombok.AllArgsConstructor;
import org.example.userservice.dtos.UserDTO;
import org.example.userservice.exceptions.NotFoundException;
import org.example.userservice.models.Session;
import org.example.userservice.models.User;
import org.example.userservice.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;



    // Get user by ID
    @Override
    public User getUser(Long id) throws NotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }

    // Update user by ID
    @Override
    public User updateUser(Long id, UserDTO userDTO) throws NotFoundException {
        User existingUser = getUser(id);
        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            existingUser.setEmail(userDTO.getEmail()); // Encode new password
        }
        if (userDTO.getFirstName() != null && !userDTO.getFirstName().isEmpty()) {
            existingUser.setFirstName(userDTO.getFirstName()); // Encode new password
        }
        if (userDTO.getLastName() != null && !userDTO.getLastName().isEmpty()) {
            existingUser.setLastName(userDTO.getLastName()); // Encode new password
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Encode new password
        }
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            existingUser.setRoles(userDTO.getRoles()); // Encode new password
        }
        return userRepository.save(existingUser);
    }

    // Delete user by ID
    @Override
    public void deleteUser(Long id) throws NotFoundException {
        User user = getUser(id);
        userRepository.delete(user);
    }

    // List all users
    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

}

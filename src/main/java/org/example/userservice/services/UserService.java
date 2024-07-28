package org.example.userservice.services;

import lombok.AllArgsConstructor;
import org.example.userservice.models.Session;
import org.example.userservice.models.User;
import org.example.userservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;


}

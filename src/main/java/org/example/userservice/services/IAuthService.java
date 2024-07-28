package org.example.userservice.services;

import org.example.userservice.dtos.UserResponseDto;
import org.example.userservice.exceptions.InvalidCredentialsException;
import org.example.userservice.exceptions.NotFoundException;
import org.example.userservice.exceptions.UserAlreadyExistsException;
import org.example.userservice.models.Session;
import org.example.userservice.models.SessionStatus;
import org.example.userservice.models.User;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    User signUp(String email, String password, String firstName, String lastName) throws NotFoundException, UserAlreadyExistsException ;

    ResponseEntity<UserResponseDto> login(String email, String password) throws NotFoundException, InvalidCredentialsException;

    void logout(String token, Long UserId);

    SessionStatus validateToken(String token, Long userId);
}

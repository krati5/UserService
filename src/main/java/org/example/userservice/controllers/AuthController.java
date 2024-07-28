package org.example.userservice.controllers;

import lombok.AllArgsConstructor;
import org.example.userservice.dtos.*;
import org.example.userservice.exceptions.InvalidCredentialsException;
import org.example.userservice.exceptions.NotFoundException;
import org.example.userservice.exceptions.UserAlreadyExistsException;
import org.example.userservice.models.SessionStatus;
import org.example.userservice.models.User;
import org.example.userservice.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody SignupRequestDto signupRequestDto) throws UserAlreadyExistsException {

        User createdUser = authService.signUp(signupRequestDto.getEmail(), signupRequestDto.getPassword(), signupRequestDto.getFirstName(), signupRequestDto.getLastName());

        logger.info("User created {}", createdUser );
        return new ResponseEntity<>(UserResponseDto.fromUser(createdUser),
                HttpStatus.OK);


    }

        @PostMapping("/login")
        public ResponseEntity<UserResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) throws InvalidCredentialsException, NotFoundException {
            return authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        }

        @PostMapping("/validate")
        public ResponseEntity<SessionStatus> validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto)   {
            SessionStatus sessionStatus = authService.validateToken(validateTokenRequestDto.getToken(), validateTokenRequestDto.getUserId());
            return new ResponseEntity<SessionStatus>(sessionStatus, HttpStatus.OK);
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto){
            authService.logout(logoutRequestDto.getToken(), logoutRequestDto.getUserId());
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
}

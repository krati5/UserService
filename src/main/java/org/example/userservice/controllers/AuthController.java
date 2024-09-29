package org.example.userservice.controllers;

import lombok.AllArgsConstructor;
import org.example.userservice.dtos.*;
import org.example.userservice.exceptions.InvalidCredentialsException;
import org.example.userservice.exceptions.NotFoundException;
import org.example.userservice.exceptions.UserAlreadyExistsException;
import org.example.userservice.models.SessionStatus;
import org.example.userservice.models.User;
import org.example.userservice.services.AuthService;
import org.example.userservice.services.IAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private IAuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody SignupRequestDto signupRequestDto) throws UserAlreadyExistsException, NotFoundException {

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
        public ResponseEntity<ValidateTokenResponseDto> validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto) throws NotFoundException {
            ValidateTokenResponseDto validateTokenResponseDto = authService.validateToken(validateTokenRequestDto.getToken(), validateTokenRequestDto.getUserId());
            return new ResponseEntity<ValidateTokenResponseDto>(validateTokenResponseDto, HttpStatus.OK);
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto){
            authService.logout(logoutRequestDto.getToken(), logoutRequestDto.getUserId());
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{userId}/roles")
    public ResponseEntity<UserResponseDto> assignRolesToUser(@PathVariable Long userId, @RequestBody RoleIdsRequestDto requestDto) {
        User user = authService.assignRolesToUser(userId, requestDto.getRoleIds());
        return ResponseEntity.ok(UserResponseDto.fromUser(user));
    }
}

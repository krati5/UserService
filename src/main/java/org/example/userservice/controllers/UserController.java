package org.example.userservice.controllers;

import org.example.userservice.dtos.UserDTO;
import org.example.userservice.dtos.UserResponseDto;
import org.example.userservice.exceptions.NotFoundException;
import org.example.userservice.models.User;
import org.example.userservice.services.IUserService;
import org.example.userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private IUserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) throws NotFoundException {
        User user = userService.getUser(id);
        return new ResponseEntity<>(UserResponseDto.fromUser(user), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) throws NotFoundException {
        User user = userService.updateUser(id, userDTO);
        return new ResponseEntity<>(UserResponseDto.fromUser(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws NotFoundException {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> listUsers() {
        List<User> users = userService.listUsers();
        List<UserResponseDto> userResponseDtos = users.stream()
                .map(UserResponseDto::fromUser)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userResponseDtos, HttpStatus.OK);
    }



}

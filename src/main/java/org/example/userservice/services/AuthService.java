package org.example.userservice.services;

import org.example.userservice.dtos.UserResponseDto;
import org.example.userservice.dtos.ValidateTokenResponseDto;
import org.example.userservice.exceptions.InvalidCredentialsException;
import org.example.userservice.exceptions.NotFoundException;
import org.example.userservice.exceptions.TokenGenerationException;
import org.example.userservice.exceptions.UserAlreadyExistsException;
import org.example.userservice.models.Role;
import org.example.userservice.models.Session;
import org.example.userservice.models.SessionStatus;
import org.example.userservice.models.User;
import org.example.userservice.repositories.RoleRepository;
import org.example.userservice.repositories.SessionRepository;
import org.example.userservice.repositories.UserRepository;
import org.example.userservice.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import java.util.*;

@Service
public class AuthService implements IAuthService {

    private JwtUtil jwtUtil;


    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private RoleRepository roleRepository;


    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, RoleRepository roleRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Override
    public User signUp(String email, String password, String firstName, String lastName) throws UserAlreadyExistsException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isEmpty()){
            throw new UserAlreadyExistsException("User with email : "+email+" already exists.");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);

        return userRepository.save(user);
    }



    @Override
    public ResponseEntity<UserResponseDto> login(String email, String password) throws NotFoundException, InvalidCredentialsException {
        // Check User is present in DB
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User with email : " + email + " not found.");
        }
        User savedUser = optionalUser.get();

        // Validate password
        if (!passwordEncoder.matches(password, savedUser.getPassword())) {
            throw new InvalidCredentialsException("Email id or password is incorrect");
        }

        String token;
        try {
            token = jwtUtil.generateToken(savedUser.getId());
        } catch (Exception e) {
            throw new TokenGenerationException("Error occurred while generating token: " + e.getMessage());
        }

        Session session = new Session();
        session.setToken(token);
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setUser(savedUser);
        session.setExpiringAt(jwtUtil.extractExpiration(token));
        sessionRepository.save(session);

        MultiValueMapAdapter<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("AUTH_TOKEN", token);


        return new ResponseEntity<UserResponseDto>(UserResponseDto.fromUser(savedUser),
                headers,
                HttpStatus.OK);
    }

    @Override
    public void logout(String token, Long userId) {

        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty()){
            return;
        }
        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.EXPIRED);
        sessionRepository.save(session);

    }

    public User getUserById(Long userId) throws NotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new NotFoundException("User with id "+ userId +" not found.");
        }
        return userOptional.get();
    }

    @Override
    public ValidateTokenResponseDto validateToken(String token, Long userId) throws NotFoundException {
        ValidateTokenResponseDto  validateTokenResponseDto= new ValidateTokenResponseDto();
        UserResponseDto userResponseDto = UserResponseDto.fromUser(getUserById(userId));
        validateTokenResponseDto.setUserResponseDto(userResponseDto);

        if(!jwtUtil.isTokenValid(token, userId)){
            validateTokenResponseDto.setSessionStatus(SessionStatus.INVALID);
            return validateTokenResponseDto;
        }

        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty()){
            validateTokenResponseDto.setSessionStatus(SessionStatus.INVALID);
            return validateTokenResponseDto;
        }

        Session session = sessionOptional.get();
        if(session.getSessionStatus()!= SessionStatus.ACTIVE){
            validateTokenResponseDto.setSessionStatus(SessionStatus.EXPIRED);
            return validateTokenResponseDto;
        }

        validateTokenResponseDto.setSessionStatus(SessionStatus.ACTIVE);
        return validateTokenResponseDto;

    }

    @Override
    public User assignRolesToUser(Long userId, Set<Long> roleIds) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Set<Role> roles = new HashSet<>();
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(role);
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }

}

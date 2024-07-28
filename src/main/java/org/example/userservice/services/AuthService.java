package org.example.userservice.services;

import org.example.userservice.dtos.UserResponseDto;
import org.example.userservice.exceptions.InvalidCredentialsException;
import org.example.userservice.exceptions.NotFoundException;
import org.example.userservice.exceptions.UserAlreadyExistsException;
import org.example.userservice.models.Session;
import org.example.userservice.models.SessionStatus;
import org.example.userservice.models.User;
import org.example.userservice.repositories.SessionRepository;
import org.example.userservice.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private SessionRepository sessionRepository;


    public AuthService(UserRepository userRepository, SessionRepository sessionRepository){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public User signUp(String email, String password, String firstName, String lastName) throws UserAlreadyExistsException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(!optionalUser.isEmpty()){
            throw new UserAlreadyExistsException("User with email : "+email+" already exists.");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);

        return userRepository.save(user);
    }

    public Date getExpiringDate(){
        // Create an instance of Calendar
        Calendar calendar = Calendar.getInstance();

        // Set the calendar to the current date and time
        calendar.setTime(new Date());

        // Add 5 days to the current date
        calendar.add(Calendar.DAY_OF_MONTH, 5);

        // Get the new date
        return calendar.getTime();

    }

    @Override
    public ResponseEntity<UserResponseDto> login(String email, String password) throws NotFoundException, InvalidCredentialsException {
        // Check User is present in DB
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new NotFoundException("User with email : "+email+" not found.");
        }
        User savedUser = optionalUser.get();

        // Validate password
        if(!bCryptPasswordEncoder.matches(password, savedUser.getPassword())){
            throw new InvalidCredentialsException("Email id or password is incorrect");
        }

        String token = "zbdheksdsdkdfkgfefjrguafjgn";

        Session session = new Session();
        session.setToken(token);
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setUser(savedUser);
        session.setExpiringAt(getExpiringDate());
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

    @Override
    public SessionStatus validateToken(String token, Long userId) {

        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty()){
            return SessionStatus.INVALID;
        }
        Session session = sessionOptional.get();
        if(session.getSessionStatus()!= SessionStatus.ACTIVE){
            return SessionStatus.EXPIRED;
        }

        if(session.getExpiringAt().before(new Date())){
            return SessionStatus.EXPIRED;
        }

        return SessionStatus.ACTIVE;

    }

}

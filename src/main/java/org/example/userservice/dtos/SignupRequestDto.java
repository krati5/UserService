package org.example.userservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.userservice.models.User;

@Getter
@Setter
public class SignupRequestDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public User toUser(){
        User user = new User();
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);

        return user;
    }
}

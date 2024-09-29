package org.example.userservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.userservice.models.SessionStatus;

@Getter
@Setter
public class ValidateTokenResponseDto {
    SessionStatus sessionStatus;
    UserResponseDto userResponseDto;
}

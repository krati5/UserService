package org.example.userservice.controllers;

import org.example.userservice.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvices {
    @ExceptionHandler({NotFoundException.class, InvalidCredentialsException.class})
    public ResponseEntity<ErrorResponseDto> exception(Exception exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(exception.getMessage());

        return new ResponseEntity(errorResponseDto, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity<ErrorResponseDto> handleUserExistsException (Exception exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(exception.getMessage());

        return new ResponseEntity(errorResponseDto, HttpStatus.CONFLICT);

    }


    @ExceptionHandler({RuntimeException.class, Exception.class, TokenGenerationException.class})
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage("An unexpected error occurred: " + exception.getMessage());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

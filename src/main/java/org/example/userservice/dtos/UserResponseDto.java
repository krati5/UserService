package org.example.userservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.userservice.models.Address;
import org.example.userservice.models.Role;
import org.example.userservice.models.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<Role> roles = new HashSet<>();
    private List<AddressResponseDTO> addresses = new ArrayList<>();


    public static UserResponseDto fromUser(User user){
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setRoles(user.getRoles());
        userResponseDto.setId(user.getId());
        userResponseDto.setAddresses(user.getAddresses().stream().map(AddressResponseDTO::fromEntity).collect(Collectors.toList()));
        return userResponseDto;
    }
}

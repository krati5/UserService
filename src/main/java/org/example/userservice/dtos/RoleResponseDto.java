package org.example.userservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.userservice.models.Role;

import java.security.Permission;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class RoleResponseDto {
    private Long id;
    private String role;

    // Method to convert Role to RoleResponseDto
    public static RoleResponseDto fromRole(Role role) {
        return new RoleResponseDto(
                role.getId(),
                role.getRole()
        );
    }
}

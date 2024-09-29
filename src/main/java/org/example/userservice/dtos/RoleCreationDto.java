package org.example.userservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.userservice.models.Role;

import java.security.Permission;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreationDto {
    private String roleName;

    // Method to convert RoleCreationDto to Role
    public Role toRole() {
        Role role = new Role();
        role.setRole(this.roleName);
        return role;
    }
}

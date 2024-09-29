package org.example.userservice.controllers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.userservice.dtos.RoleCreationDto;
import org.example.userservice.dtos.RoleResponseDto;
import org.example.userservice.exceptions.NotFoundException;
import org.example.userservice.models.Role;
import org.example.userservice.services.IRoleService;
import org.example.userservice.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private IRoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleResponseDto>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        List<RoleResponseDto> roleResponseDtos = roles.stream()
                .map(RoleResponseDto::fromRole)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roleResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable Long id) throws NotFoundException {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(RoleResponseDto.fromRole(role));
    }

    @PostMapping
    public ResponseEntity<RoleResponseDto> createRole(@RequestBody RoleCreationDto roleCreationDto) {
        Role createdRole = roleService.createRole(roleCreationDto.toRole());
        return new ResponseEntity<>(RoleResponseDto.fromRole(createdRole), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDto> updateRole(@PathVariable Long id, @RequestBody RoleCreationDto roleCreationDto) throws NotFoundException {
        Role updatedRole = roleService.updateRole(id, roleCreationDto.toRole());
        return ResponseEntity.ok(RoleResponseDto.fromRole(updatedRole));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) throws NotFoundException {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }


}

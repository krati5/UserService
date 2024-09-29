package org.example.userservice.dtos;


import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RoleIdsRequestDto {
    private Set<Long> roleIds;
}

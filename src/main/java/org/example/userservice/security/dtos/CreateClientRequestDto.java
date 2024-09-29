package org.example.userservice.security.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateClientRequestDto {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String postLogoutRedirectUri;

}

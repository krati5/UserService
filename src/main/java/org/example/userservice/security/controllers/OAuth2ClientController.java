package org.example.userservice.security.controllers;

import org.example.userservice.security.dtos.CreateClientRequestDto;
import org.example.userservice.security.services.OAuth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class OAuth2ClientController {

    @Autowired
    private OAuth2ClientService oAuth2ClientService;

    @PostMapping("/create")
    public ResponseEntity<String> createClient(@RequestBody CreateClientRequestDto request){
        try {
            oAuth2ClientService.insertNewClientToDb(request.getClientId(), request.getClientSecret(), request.getRedirectUri(), request.getPostLogoutRedirectUri());
            return ResponseEntity.ok("Client created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating client: " + e.getMessage());
        }
    }
}

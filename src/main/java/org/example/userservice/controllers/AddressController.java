package org.example.userservice.controllers;


import org.example.userservice.dtos.AddressRequestDTO;
import org.example.userservice.dtos.AddressResponseDTO;
import org.example.userservice.exceptions.NotFoundException;
import org.example.userservice.models.Address;
import org.example.userservice.services.IAddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final IAddressService addressService;


    public AddressController(IAddressService addressService) {
        this.addressService = addressService;
    }


    // Endpoint to add an address to a user
    @PostMapping("/user/{userId}")
    public ResponseEntity<AddressResponseDTO> addAddressToUser(@PathVariable Long userId, @RequestBody AddressRequestDTO addressRequest) throws NotFoundException {
        Address addedAddress = addressService.addAddressToUser(userId, addressRequest.toEntity());
        return ResponseEntity.ok(AddressResponseDTO.fromEntity(addedAddress));
    }

    // Endpoint to find all addresses for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponseDTO>> getAllAddressesByUserId(@PathVariable Long userId) throws NotFoundException {
        List<Address> addresses = addressService.findAllAddressesByUserId(userId);
        List<AddressResponseDTO> addressDTOs = addresses.stream().map(AddressResponseDTO::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(addressDTOs);
    }

    // Endpoint to update an address
    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> updateAddress(@PathVariable Long addressId, @RequestBody AddressRequestDTO addressRequest) throws NotFoundException {
        Address updatedAddress = addressService.updateAddress(addressId, addressRequest.toEntity());
        return ResponseEntity.ok(AddressResponseDTO.fromEntity(updatedAddress));
    }
}

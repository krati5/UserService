package org.example.userservice.services;

import org.example.userservice.exceptions.NotFoundException;
import org.example.userservice.models.Address;

import java.util.List;

public interface IAddressService {
    Address addAddressToUser(Long userId, Address address) throws NotFoundException;

    // Method to find all addresses for a user
    List<Address> findAllAddressesByUserId(Long userId) throws NotFoundException;

    // Method to update an existing address
    Address updateAddress(Long addressId, Address newAddressDetails) throws NotFoundException;
}

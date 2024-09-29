package org.example.userservice.services;

import lombok.AllArgsConstructor;
import org.example.userservice.exceptions.NotFoundException;
import org.example.userservice.models.Address;
import org.example.userservice.models.User;
import org.example.userservice.repositories.AddressRepository;
import org.example.userservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AddressService implements IAddressService {
    private AddressRepository addressRepository;
    private UserRepository userRepository;

    @Override
    public Address addAddressToUser(Long userId, Address address) throws NotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            address.setUser(user);  // Set the user reference in the Address
            user.getAddresses().add(address);
            userRepository.save(user);
            return address;
        } else {
            throw new NotFoundException("User not found");
        }
    }

    // Method to find all addresses for a user
    @Override
    public List<Address> findAllAddressesByUserId(Long userId) throws NotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get().getAddresses();
        } else {
            throw new NotFoundException("User not found");
        }
    }

    // Method to update an existing address
    @Override
    public Address updateAddress(Long addressId, Address newAddressDetails) throws NotFoundException {
        Optional<Address> addressOptional = addressRepository.findById(addressId);
        if (addressOptional.isPresent()) {
            Address existingAddress = addressOptional.get();
            // Update the address details
            existingAddress.setStreet(newAddressDetails.getStreet());
            existingAddress.setCity(newAddressDetails.getCity());
            existingAddress.setState(newAddressDetails.getState());
            existingAddress.setPostalCode(newAddressDetails.getPostalCode());
            existingAddress.setCountry(newAddressDetails.getCountry());
            existingAddress.setPhoneNumber(newAddressDetails.getPhoneNumber());
            return addressRepository.save(existingAddress);
        } else {
            throw new NotFoundException("Address not found");
        }
    }


}

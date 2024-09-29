package org.example.userservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.userservice.models.Address;

@Getter
@Setter
public class AddressRequestDTO {
    private String houseNo;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phoneNumber;

    // Convert from DTO to Entity
    public Address toEntity() {
        Address address = new Address();
        address.setHouseNo(this.getHouseNo());
        address.setStreet(this.getStreet());
        address.setCity(this.getCity());
        address.setState(this.getState());
        address.setPostalCode(this.getPostalCode());
        address.setCountry(this.getCountry());
        address.setPhoneNumber(this.getPhoneNumber());
        return address;
    }
}

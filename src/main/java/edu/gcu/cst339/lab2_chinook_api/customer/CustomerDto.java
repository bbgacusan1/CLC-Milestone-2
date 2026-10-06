package edu.gcu.cst339.lab2_chinook_api.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * CustomerDto
 */
public record CustomerDto(
    Integer customerId,
        @NotBlank @Size(max = 40) String firstName,
        @NotBlank @Size(max = 20) String lastName,
        @Size(max = 80) String company,
        @Size(max = 70) String address,
        @Size(max = 40) String city,
        @Size(max = 40) String state,
        @Size(max = 40) String country,
        @Size(max = 10) String postalCode,
        @Size(max = 24) String phone,
        @Size(max = 24) String fax,
        @NotBlank @Email @Size(max = 60) String email,
        Integer supportRepId) {

    static CustomerDto fromEntity(Customer c) {
        return new CustomerDto(
            c.getCustomerId(),
            c.getFirstName(), 
            c.getLastName(),
            c.getCompany(),
            c.getAddress(),
            c.getCity(), 
            c.getState(),
            c.getCountry(),
            c.getPostalCode(),
            c.getPhone(),
            c.getFax(),
            c.getEmail(),
            c.getSupportRepId()); 
    }
}
    

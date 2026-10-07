package edu.gcu.cst339.lab2_chinook_api.employee;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmployeeDto(
    Integer employeeId,
        @NotBlank @Size(max = 20) String lastName,
        @NotBlank @Size(max = 20) String firstName,
        @Size(max = 30) String title,
        Integer reportsTo,
        LocalDateTime birthDate,
        LocalDateTime hireDate,
        @Size(max = 70) String address,
        @Size(max = 40) String city,
        @Size(max = 40) String state,
        @Size(max = 40) String country,
        @Size(max = 10) String postalCode,
        @Size(max = 24) String phone,
        @Size(max = 24) String fax,
        @Email @Size(max = 60) String email) {

    static EmployeeDto fromEntity(Employee e) {
        return new EmployeeDto(
                e.getEmployeeId(),
                e.getLastName(),
                e.getFirstName(),
                e.getTitle(),
                e.getReportsTo(),
                e.getBirthDate(),
                e.getHireDate(),
                e.getAddress(),
                e.getCity(),
                e.getState(),
                e.getCountry(),
                e.getPostalCode(),
                e.getPhone(),
                e.getFax(),
                e.getEmail());
    }
}

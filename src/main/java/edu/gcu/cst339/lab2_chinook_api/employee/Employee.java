package edu.gcu.cst339.lab2_chinook_api.employee;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "employee")
class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;

    @NotNull
    @Size(max = 20)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Size(max = 20)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Size(max = 30)
    @Column(name = "title")
    private String title;

    @Column(name = "reports_to")
    private Integer reportsTo;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Column(name = "hire_date")
    private LocalDateTime hireDate;

    @Size(max = 70)
    @Column(name = "address")
    private String address;

    @Size(max = 40)
    @Column(name = "city")
    private String city;

    @Size(max = 40)
    @Column(name = "state")
    private String state;

    @Size(max = 40)
    @Column(name = "country")
    private String country;

    @Size(max = 10)
    @Column(name = "postal_code")
    private String postalCode;

    @Size(max = 24)
    @Column(name = "phone")
    private String phone;

    @Size(max = 24)
    @Column(name = "fax")
    private String fax;

    @Size(max = 60)
    @Column(name = "email")
    private String email;

    public Employee() {
    }

    public Integer getEmployeeId() { return employeeId; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getReportsTo() { return reportsTo; }
    public void setReportsTo(Integer reportsTo) { this.reportsTo = reportsTo; }

    public LocalDateTime getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDateTime birthDate) { this.birthDate = birthDate; }

    public LocalDateTime getHireDate() { return hireDate; }
    public void setHireDate(LocalDateTime hireDate) { this.hireDate = hireDate; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getFax() { return fax; }
    public void setFax(String fax) { this.fax = fax; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
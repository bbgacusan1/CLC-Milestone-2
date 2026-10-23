package edu.gcu.cst339.lab2_chinook_api.employee;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * JPA entity mapped to the Chinook {@code employee} table.
 *
 * <p>Stores an employee's name, job title, birth and hire dates, address and
 * contact details. First and last name are required. {@code reportsTo} is a
 * self-referencing foreign key to {@code employee.employee_id} identifying the
 * employee's manager; it is {@code null} for the top of the hierarchy.
 * Customers refer to their support employee through
 * {@code customer.support_rep_id}.</p>
 */
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

    /**
     * Creates an empty employee. Used by JPA and by the service before fields are
     * copied in from a DTO.
     */
    public Employee() {
    }

    /** @return the database-generated employee ID, or {@code null} if not yet saved */
    public Integer getEmployeeId() { return employeeId; }

    /** @return the employee's last name */
    public String getLastName() { return lastName; }
    /** @param lastName the employee's last name (required, at most 20 characters) */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /** @return the employee's first name */
    public String getFirstName() { return firstName; }
    /** @param firstName the employee's first name (required, at most 20 characters) */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** @return the employee's job title, such as "Sales Support Agent" */
    public String getTitle() { return title; }
    /** @param title the job title (optional, at most 30 characters) */
    public void setTitle(String title) { this.title = title; }

    /** @return the ID of this employee's manager, or {@code null} if the employee reports to no one */
    public Integer getReportsTo() { return reportsTo; }
    /** @param reportsTo the ID of an existing employee who manages this employee, or {@code null} */
    public void setReportsTo(Integer reportsTo) { this.reportsTo = reportsTo; }

    /** @return the employee's date of birth */
    public LocalDateTime getBirthDate() { return birthDate; }
    /** @param birthDate the date of birth (optional) */
    public void setBirthDate(LocalDateTime birthDate) { this.birthDate = birthDate; }

    /** @return the date the employee was hired */
    public LocalDateTime getHireDate() { return hireDate; }
    /** @param hireDate the hire date (optional) */
    public void setHireDate(LocalDateTime hireDate) { this.hireDate = hireDate; }

    /** @return the employee's street address */
    public String getAddress() { return address; }
    /** @param address the street address (optional, at most 70 characters) */
    public void setAddress(String address) { this.address = address; }

    /** @return the employee's city */
    public String getCity() { return city; }
    /** @param city the city (optional, at most 40 characters) */
    public void setCity(String city) { this.city = city; }

    /** @return the employee's state or province */
    public String getState() { return state; }
    /** @param state the state or province (optional, at most 40 characters) */
    public void setState(String state) { this.state = state; }

    /** @return the employee's country */
    public String getCountry() { return country; }
    /** @param country the country (optional, at most 40 characters) */
    public void setCountry(String country) { this.country = country; }

    /** @return the employee's postal code */
    public String getPostalCode() { return postalCode; }
    /** @param postalCode the postal code (optional, at most 10 characters) */
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    /** @return the employee's phone number */
    public String getPhone() { return phone; }
    /** @param phone the phone number (optional, at most 24 characters) */
    public void setPhone(String phone) { this.phone = phone; }

    /** @return the employee's fax number */
    public String getFax() { return fax; }
    /** @param fax the fax number (optional, at most 24 characters) */
    public void setFax(String fax) { this.fax = fax; }

    /** @return the employee's email address */
    public String getEmail() { return email; }
    /** @param email the email address (optional, at most 60 characters) */
    public void setEmail(String email) { this.email = email; }
}
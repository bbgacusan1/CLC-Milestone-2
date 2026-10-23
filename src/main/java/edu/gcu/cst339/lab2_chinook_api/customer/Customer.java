package edu.gcu.cst339.lab2_chinook_api.customer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * JPA entity mapped to the Chinook {@code customer} table.
 *
 * <p>Stores a customer's name, company, postal address, phone, fax and email.
 * First name, last name and email are required. {@code supportRepId} is an
 * optional foreign key to {@code employee.employee_id} identifying the sales
 * support agent assigned to the customer. Invoices refer to a customer through
 * {@code invoice.customer_id}.</p>
 */
@Entity
@Table(name = "customer")
class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @NotNull
    @Size(max = 40)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Size(max = 20)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Size(max = 80)
    @Column(name = "company")
    private String company;

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

    @NotNull
    @Size(max = 60)
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "support_rep_id")
    private Integer supportRepId;

    /**
     * Creates an empty customer. Used by JPA and by the service before fields are
     * copied in from a DTO.
     */
    public Customer() {
    }

    /** @return the database-generated customer ID, or {@code null} if not yet saved */
    public Integer getCustomerId() { return customerId; }

    /** @return the customer's first name */
    public String getFirstName() { return firstName; }
    /** @param firstName the customer's first name (required, at most 40 characters) */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** @return the customer's last name */
    public String getLastName() { return lastName; }
    /** @param lastName the customer's last name (required, at most 20 characters) */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /** @return the customer's company, or {@code null} if none */
    public String getCompany() { return company; }
    /** @param company the customer's company (optional, at most 80 characters) */
    public void setCompany(String company) { this.company = company; }

    /** @return the customer's street address */
    public String getAddress() { return address; }
    /** @param address the street address (optional, at most 70 characters) */
    public void setAddress(String address) { this.address = address; }

    /** @return the customer's city */
    public String getCity() { return city; }
    /** @param city the city (optional, at most 40 characters) */
    public void setCity(String city) { this.city = city; }

    /** @return the customer's state or province */
    public String getState() { return state; }
    /** @param state the state or province (optional, at most 40 characters) */
    public void setState(String state) { this.state = state; }

    /** @return the customer's country */
    public String getCountry() { return country; }
    /** @param country the country (optional, at most 40 characters) */
    public void setCountry(String country) { this.country = country; }

    /** @return the customer's postal code */
    public String getPostalCode() { return postalCode; }
    /** @param postalCode the postal code (optional, at most 10 characters) */
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    /** @return the customer's phone number */
    public String getPhone() { return phone; }
    /** @param phone the phone number (optional, at most 24 characters) */
    public void setPhone(String phone) { this.phone = phone; }

    /** @return the customer's fax number */
    public String getFax() { return fax; }
    /** @param fax the fax number (optional, at most 24 characters) */
    public void setFax(String fax) { this.fax = fax; }

    /** @return the customer's email address */
    public String getEmail() { return email; }
    /** @param email the email address (required, at most 60 characters) */
    public void setEmail(String email) { this.email = email; }

    /** @return the ID of the employee who supports this customer, or {@code null} if none */
    public Integer getSupportRepId() { return supportRepId; }
    /** @param supportRepId the ID of an existing employee to assign as support representative, or {@code null} */
    public void setSupportRepId(Integer supportRepId) { this.supportRepId = supportRepId; }
}
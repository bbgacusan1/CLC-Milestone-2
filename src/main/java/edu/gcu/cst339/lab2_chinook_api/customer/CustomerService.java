package edu.gcu.cst339.lab2_chinook_api.customer;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for Chinook customers.
 *
 * <p>Converts between {@link Customer} entities and {@link CustomerDto} objects,
 * throws {@link CustomerNotFoundException} for unknown IDs, and runs each
 * operation in a transaction. Create and update share one helper that copies
 * every editable field from the DTO to the entity.</p>
 */
@Service
class CustomerService {
    private final CustomerRepository customerRepository;

    /**
     * Creates the service. Spring injects the repository through this constructor.
     *
     * @param customerRepository the repository used to read and write customers
     */
    CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository; 
    }

    /**
     * Returns every customer in the database.
     *
     * @return all customers as DTOs; an empty list if the table is empty
     */
    @Transactional(readOnly = true)
    List<CustomerDto> findAll() {
        return customerRepository.findAll().stream()
                .map(CustomerDto::fromEntity)
                .toList();
    }

    /**
     * Returns the customer with the given ID.
     *
     * @param id the customer ID to look up
     * @return the matching customer as a DTO
     * @throws CustomerNotFoundException if no customer has the given ID
     */
    @Transactional(readOnly = true)
    CustomerDto findById(Integer id) {
        return customerRepository.findById(id)
                .map(CustomerDto::fromEntity)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    /**
     * Saves a new customer. Any {@code customerId} in the DTO is ignored because
     * the database generates the ID.
     *
     * @param dto the details of the new customer
     * @return the saved customer, including its generated ID
     */
    @Transactional
    CustomerDto create(CustomerDto dto) {
        Customer customer = new Customer();
        copyFields(dto, customer);
        return CustomerDto.fromEntity(customerRepository.save(customer));
    }

    /**
     * Replaces every editable field of an existing customer.
     *
     * <p>The entity is changed inside the transaction and JPA writes the change
     * when the transaction commits, so no explicit {@code save} call is needed.</p>
     *
     * @param id  the ID of the customer to update
     * @param dto the new values; any {@code customerId} in the DTO is ignored
     * @return the updated customer
     * @throws CustomerNotFoundException if no customer has the given ID
     */
    @Transactional
    CustomerDto update(Integer id, CustomerDto dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        copyFields(dto, customer);
        return CustomerDto.fromEntity(customer);
    }

    /**
     * Deletes the customer with the given ID.
     *
     * @param id the ID of the customer to delete
     * @throws CustomerNotFoundException if no customer has the given ID
     */
    @Transactional
    void delete(Integer id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
    }

    private void copyFields(CustomerDto dto, Customer customer) {
        customer.setFirstName(dto.firstName());
        customer.setLastName(dto.lastName());
        customer.setCompany(dto.company());
        customer.setAddress(dto.address());
        customer.setCity(dto.city());
        customer.setState(dto.state());
        customer.setCountry(dto.country());
        customer.setPostalCode(dto.postalCode());
        customer.setPhone(dto.phone());
        customer.setFax(dto.fax());
        customer.setEmail(dto.email());
        customer.setSupportRepId(dto.supportRepId());
    }
}

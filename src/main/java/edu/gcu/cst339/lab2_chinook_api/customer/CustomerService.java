package edu.gcu.cst339.lab2_chinook_api.customer;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class CustomerService {
    private final CustomerRepository customerRepository;

    CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository; 
    }

    @Transactional(readOnly = true)
    List<CustomerDto> findAll() {
        return customerRepository.findAll().stream()
                .map(CustomerDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    CustomerDto findById(Integer id) {
        return customerRepository.findById(id)
                .map(CustomerDto::fromEntity)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Transactional
    CustomerDto create(CustomerDto dto) {
        Customer customer = new Customer();
        copyFields(dto, customer);
        return CustomerDto.fromEntity(customerRepository.save(customer));
    }

    @Transactional
    CustomerDto update(Integer id, CustomerDto dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        copyFields(dto, customer);
        return CustomerDto.fromEntity(customer);
    }

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

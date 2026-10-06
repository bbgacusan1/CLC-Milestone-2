package edu.gcu.cst339.lab2_chinook_api.customer;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
class CustomerController {
    private final CustomerService customerService;

    CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    List<CustomerDto> findAll() {
        return customerService.findAll(); 
    }

    @GetMapping("/{id}")
    CustomerDto findById(@PathVariable Integer id) {
        return customerService.findById(id); 
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CustomerDto create(@Valid @RequestBody CustomerDto dto) {
        return customerService.create(dto);
    }

     @PutMapping("/{id}")
    ResponseEntity<CustomerDto> update(@PathVariable Integer id, @RequestBody CustomerDto dto) {
        CustomerDto updated = customerService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Integer id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

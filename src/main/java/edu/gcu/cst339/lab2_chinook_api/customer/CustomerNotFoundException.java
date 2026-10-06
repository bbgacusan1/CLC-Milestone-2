

package edu.gcu.cst339.lab2_chinook_api.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class CustomerNotFoundException extends RuntimeException {

    CustomerNotFoundException(Integer id) {
        super("Customer not found with id " + id);
    }
}
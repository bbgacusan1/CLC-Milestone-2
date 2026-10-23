

package edu.gcu.cst339.lab2_chinook_api.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when no customer exists with the requested ID.
 *
 * <p>The {@code @ResponseStatus} annotation tells Spring MVC to answer the
 * request with HTTP 404 Not Found when this exception reaches the controller.</p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class CustomerNotFoundException extends RuntimeException {

    /**
     * Creates the exception with a message that includes the missing ID.
     *
     * @param id the customer ID that was not found
     */
    CustomerNotFoundException(Integer id) {
        super("Customer not found with id " + id);
    }
}
package edu.gcu.cst339.lab2_chinook_api.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when no employee exists with the requested ID.
 *
 * <p>The {@code @ResponseStatus} annotation tells Spring MVC to answer the
 * request with HTTP 404 Not Found when this exception reaches the controller.</p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class EmployeeNotFoundException extends RuntimeException {

    /**
     * Creates the exception with a message that includes the missing ID.
     *
     * @param id the employee ID that was not found
     */
    EmployeeNotFoundException(Integer id) {
        super("Employee not found with id " + id);
    }
}
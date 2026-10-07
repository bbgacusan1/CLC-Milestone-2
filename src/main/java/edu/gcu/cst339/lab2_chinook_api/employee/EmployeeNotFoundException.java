package edu.gcu.cst339.lab2_chinook_api.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class EmployeeNotFoundException extends RuntimeException {

    EmployeeNotFoundException(Integer id) {
        super("Employee not found with id " + id);
    }
}
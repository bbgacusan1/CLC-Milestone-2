package edu.gcu.cst339.lab2_chinook_api.employee;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
class EmployeeController {

    private final EmployeeService employeeService;

    EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    List<EmployeeDto> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    EmployeeDto findById(@PathVariable Integer id) {
        return employeeService.findById(id);
    }

    @PostMapping
    ResponseEntity<EmployeeDto> create(@Valid @RequestBody EmployeeDto dto) {
        EmployeeDto created = employeeService.create(dto);
        URI location = URI.create("/api/employees/" + created.employeeId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    ResponseEntity<EmployeeDto> update(@PathVariable Integer id, @Valid @RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(employeeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Integer id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
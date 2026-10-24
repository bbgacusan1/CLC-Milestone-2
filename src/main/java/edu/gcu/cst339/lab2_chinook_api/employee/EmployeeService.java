package edu.gcu.cst339.lab2_chinook_api.employee;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for Chinook employees.
 *
 * <p>Converts between {@link Employee} entities and {@link EmployeeDto} objects,
 * throws {@link EmployeeNotFoundException} for unknown IDs, and runs each
 * operation in a transaction. Create and update share one helper that copies
 * every editable field from the DTO to the entity.</p>
 */
@Service
class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * Creates the service. Spring injects the repository through this constructor.
     *
     * @param employeeRepository the repository used to read and write employees
     */
    EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Returns every employee in the database.
     *
     * @return all employees as DTOs; an empty list if the table is empty
     */
    @Transactional(readOnly = true)
    List<EmployeeDto> findAll() {
        return employeeRepository.findAll().stream()
                .map(EmployeeDto::fromEntity)
                .toList();
    }

    /**
     * Returns the employee with the given ID.
     *
     * @param id the employee ID to look up
     * @return the matching employee as a DTO
     * @throws EmployeeNotFoundException if no employee has the given ID
     */
    @Transactional(readOnly = true)
    EmployeeDto findById(Integer id) {
        return employeeRepository.findById(id)
                .map(EmployeeDto::fromEntity)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    /**
     * Saves a new employee. Any {@code employeeId} in the DTO is ignored because
     * the database generates the ID.
     *
     * @param dto the details of the new employee
     * @return the saved employee, including its generated ID
     */
    @Transactional
    EmployeeDto create(EmployeeDto dto) {
        Employee employee = new Employee();
        copyFields(dto, employee);
        return EmployeeDto.fromEntity(employeeRepository.save(employee));
    }

    /**
     * Replaces every editable field of an existing employee.
     *
     * <p>The entity is changed inside the transaction and JPA writes the change
     * when the transaction commits, so no explicit {@code save} call is needed.</p>
     *
     * @param id  the ID of the employee to update
     * @param dto the new values; any {@code employeeId} in the DTO is ignored
     * @return the updated employee
     * @throws EmployeeNotFoundException if no employee has the given ID
     */
    @Transactional
    EmployeeDto update(Integer id, EmployeeDto dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        copyFields(dto, employee);
        return EmployeeDto.fromEntity(employee);
    }

    /**
     * Deletes the employee with the given ID.
     *
     * @param id the ID of the employee to delete
     * @throws EmployeeNotFoundException if no employee has the given ID
     */
    @Transactional
    void delete(Integer id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        employeeRepository.deleteById(id);
    }

    private void copyFields(EmployeeDto dto, Employee employee) {
        employee.setLastName(dto.lastName());
        employee.setFirstName(dto.firstName());
        employee.setTitle(dto.title());
        employee.setReportsTo(dto.reportsTo());
        employee.setBirthDate(dto.birthDate());
        employee.setHireDate(dto.hireDate());
        employee.setAddress(dto.address());
        employee.setCity(dto.city());
        employee.setState(dto.state());
        employee.setCountry(dto.country());
        employee.setPostalCode(dto.postalCode());
        employee.setPhone(dto.phone());
        employee.setFax(dto.fax());
        employee.setEmail(dto.email());
    }
}
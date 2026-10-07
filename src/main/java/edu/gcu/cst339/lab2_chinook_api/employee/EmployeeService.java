package edu.gcu.cst339.lab2_chinook_api.employee;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class EmployeeService {

    private final EmployeeRepository employeeRepository;

    EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    List<EmployeeDto> findAll() {
        return employeeRepository.findAll().stream()
                .map(EmployeeDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    EmployeeDto findById(Integer id) {
        return employeeRepository.findById(id)
                .map(EmployeeDto::fromEntity)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Transactional
    EmployeeDto create(EmployeeDto dto) {
        Employee employee = new Employee();
        copyFields(dto, employee);
        return EmployeeDto.fromEntity(employeeRepository.save(employee));
    }

    @Transactional
    EmployeeDto update(Integer id, EmployeeDto dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        copyFields(dto, employee);
        return EmployeeDto.fromEntity(employee);
    }

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
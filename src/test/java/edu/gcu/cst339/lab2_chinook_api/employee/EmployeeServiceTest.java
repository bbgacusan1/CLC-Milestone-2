package edu.gcu.cst339.lab2_chinook_api.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee(int id, String first, String last, String title) {
        Employee e = new Employee();
        ReflectionTestUtils.setField(e, "employeeId", id);
        e.setFirstName(first);
        e.setLastName(last);
        e.setTitle(title);
        return e;
    }

    private EmployeeDto dto(String first, String last, String title, String city) {
        return new EmployeeDto(null, last, first, title, 2,
                null, LocalDateTime.of(2026, 10, 1, 0, 0),
                null, city, null, null, null, null, null, null);
    }

    @Test
    void findAll_returnsAllEmployeesAsDtos() {
        when(employeeRepository.findAll()).thenReturn(List.of(
                employee(1, "Andrew", "Adams", "General Manager"),
                employee(2, "Nancy", "Edwards", "Sales Manager")));

        List<EmployeeDto> result = employeeService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).firstName()).isEqualTo("Andrew");
        assertThat(result.get(1).employeeId()).isEqualTo(2);
    }

    @Test
    void findById_existingId_returnsDto() {
        when(employeeRepository.findById(2))
                .thenReturn(Optional.of(employee(2, "Nancy", "Edwards", "Sales Manager")));

        EmployeeDto result = employeeService.findById(2);

        assertThat(result.employeeId()).isEqualTo(2);
        assertThat(result.title()).isEqualTo("Sales Manager");
    }

    @Test
    void findById_missingId_throwsNotFound() {
        when(employeeRepository.findById(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.findById(99999))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void create_copiesFieldsAndReturnsDtoWithGeneratedId() {
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee toSave = invocation.getArgument(0);
            ReflectionTestUtils.setField(toSave, "employeeId", 9);
            return toSave;
        });

        EmployeeDto result = employeeService.create(
                dto("Test", "Employee", "Sales Support Agent", "Phoenix"));

        assertThat(result.employeeId()).isEqualTo(9);
        assertThat(result.lastName()).isEqualTo("Employee");
        assertThat(result.reportsTo()).isEqualTo(2);
        assertThat(result.hireDate()).isEqualTo(LocalDateTime.of(2026, 10, 1, 0, 0));
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void update_existingId_changesFieldsButNotId() {
        Employee existing = employee(4, "Old", "Name", "Agent");
        when(employeeRepository.findById(4)).thenReturn(Optional.of(existing));

        EmployeeDto result = employeeService.update(4,
                dto("New", "Name", "IT Staff", "Glendale"));

        assertThat(result.employeeId()).isEqualTo(4);
        assertThat(existing.getFirstName()).isEqualTo("New");
        assertThat(existing.getTitle()).isEqualTo("IT Staff");
        assertThat(existing.getCity()).isEqualTo("Glendale");
    }

    @Test
    void update_missingId_throwsNotFound() {
        when(employeeRepository.findById(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.update(99999,
                dto("X", "Y", null, null)))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void delete_existingId_deletesEmployee() {
        when(employeeRepository.existsById(4)).thenReturn(true);

        employeeService.delete(4);

        verify(employeeRepository).deleteById(4);
    }

    @Test
    void delete_missingId_throwsNotFoundAndDeletesNothing() {
        when(employeeRepository.existsById(99999)).thenReturn(false);

        assertThatThrownBy(() -> employeeService.delete(99999))
                .isInstanceOf(EmployeeNotFoundException.class);
        verify(employeeRepository, never()).deleteById(any());
    }
}
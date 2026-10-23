package edu.gcu.cst339.lab2_chinook_api.employee;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Employee} entities, keyed by the
 * {@code Integer} employee ID.
 *
 * <p>Spring generates the implementation at runtime; only the inherited
 * CRUD methods are used.</p>
 */
interface EmployeeRepository extends JpaRepository<Employee, Integer> { } 
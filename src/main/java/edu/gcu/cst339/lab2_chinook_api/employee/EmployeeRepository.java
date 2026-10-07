package edu.gcu.cst339.lab2_chinook_api.employee;

import org.springframework.data.jpa.repository.JpaRepository;

interface EmployeeRepository extends JpaRepository<Employee, Integer> { } 
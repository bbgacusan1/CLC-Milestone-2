package edu.gcu.cst339.lab2_chinook_api.customer;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for customer entities, keyed by the
 * {@code Integer} customer ID.
 *
 * <p>Spring generates the implementation at runtime; only the inherited
 * CRUD methods are used.</p>
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    
}

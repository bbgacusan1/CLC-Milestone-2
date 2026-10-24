package edu.gcu.cst339.lab2_chinook_api.invoice;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Invoice} entities, keyed by the
 * {@code Integer} invoice ID.
 *
 * <p>Spring generates the implementation at runtime; only the inherited
 * CRUD methods are used.</p>
 */
interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    
}
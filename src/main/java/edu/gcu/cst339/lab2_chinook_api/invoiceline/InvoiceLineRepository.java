package edu.gcu.cst339.lab2_chinook_api.invoiceline;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link InvoiceLine} entities, keyed by the
 * {@code Integer} invoice line ID.
 *
 * <p>Spring generates the implementation at runtime; only the inherited
 * CRUD methods are used.</p>
 */
interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Integer> {
    
}
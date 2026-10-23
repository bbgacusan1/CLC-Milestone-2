package edu.gcu.cst339.lab2_chinook_api.mediatype;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link MediaType} entities, keyed by the
 * {@code Integer} media type ID.
 *
 * <p>Spring generates the implementation at runtime; only the inherited
 * CRUD methods are used.</p>
 */
interface MediaTypeRepository extends JpaRepository<MediaType, Integer> {
    
}

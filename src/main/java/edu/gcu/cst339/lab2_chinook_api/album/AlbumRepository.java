package edu.gcu.cst339.lab2_chinook_api.album;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Album} entities, keyed by the
 * {@code Integer} album ID.
 *
 * <p>Spring generates the implementation at runtime, providing standard
 * operations such as {@code findAll}, {@code findById}, {@code save},
 * {@code existsById} and {@code deleteById}. No custom queries are needed.</p>
 */
interface AlbumRepository extends JpaRepository<Album, Integer> {
    
}

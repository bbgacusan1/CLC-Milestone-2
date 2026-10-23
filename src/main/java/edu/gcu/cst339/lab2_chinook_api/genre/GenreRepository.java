package edu.gcu.cst339.lab2_chinook_api.genre;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Genre} entities, keyed by the
 * {@code Integer} genre ID.
 *
 * <p>Spring generates the implementation at runtime; only the inherited
 * CRUD methods are used.</p>
 */
interface GenreRepository extends JpaRepository<Genre, Integer> {
}
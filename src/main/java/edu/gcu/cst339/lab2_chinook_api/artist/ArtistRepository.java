package edu.gcu.cst339.lab2_chinook_api.artist;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Artist} entities, keyed by the
 * {@code Integer} artist ID.
 *
 * <p>Spring generates the implementation at runtime; only the inherited
 * CRUD methods are used.</p>
 */
interface ArtistRepository extends JpaRepository<Artist, Integer> { }

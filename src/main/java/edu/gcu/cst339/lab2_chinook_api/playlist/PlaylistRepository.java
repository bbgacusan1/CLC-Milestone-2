package edu.gcu.cst339.lab2_chinook_api.playlist;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Playlist} entities, keyed by the
 * {@code Integer} playlist ID.
 *
 * <p>Spring generates the implementation at runtime; only the inherited
 * CRUD methods are used.</p>
 */
interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
        
}

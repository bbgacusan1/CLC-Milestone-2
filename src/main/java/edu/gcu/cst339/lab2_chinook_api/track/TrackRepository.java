package edu.gcu.cst339.lab2_chinook_api.track;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Track} entities, keyed by the
 * {@code Integer} track ID.
 *
 * <p>Spring generates the implementation at runtime; only the inherited
 * CRUD methods are used.</p>
 */
interface TrackRepository extends JpaRepository<Track, Integer> {
        
}

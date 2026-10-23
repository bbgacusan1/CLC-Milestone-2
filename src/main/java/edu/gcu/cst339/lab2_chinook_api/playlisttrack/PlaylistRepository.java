package edu.gcu.cst339.lab2_chinook_api.playlisttrack;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.gcu.cst339.lab2_chinook_api.playlisttrack.PlaylistTrack.PlaylistTrackId;

/**
 * Spring Data JPA repository for {@link PlaylistTrack} rows.
 *
 * <p>The first type argument is the entity and the second is the key class:
 * because the primary key is a pair of IDs, the key type is
 * {@link PlaylistTrack.PlaylistTrackId} rather than a single {@code Integer}.
 * Spring generates the implementation at runtime.</p>
 */
interface PlaylistTrackRepository extends JpaRepository<PlaylistTrack, PlaylistTrackId> {

}
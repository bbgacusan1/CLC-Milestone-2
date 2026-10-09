package edu.gcu.cst339.lab2_chinook_api.playlisttrack;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.gcu.cst339.lab2_chinook_api.playlisttrack.PlaylistTrack.PlaylistTrackId;

// First type: the entity. Second type: the key class (the id is a pair, not one Integer).
interface PlaylistTrackRepository extends JpaRepository<PlaylistTrack, PlaylistTrackId> {

}
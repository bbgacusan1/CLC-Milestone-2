package edu.gcu.cst339.lab2_chinook_api.playlisttrack;

import jakarta.validation.constraints.NotNull;   // field must not be null

// Both ids come from the client (they are the primary key), so both are required.
record PlaylistTrackDto(
    @NotNull Integer playlistId,   // which playlist
    @NotNull Integer trackId       // which track is on that playlist
) {
    // Entity -> DTO: copies the two ids out of the entity
    static PlaylistTrackDto fromEntity(PlaylistTrack playlistTrack) {
        return new PlaylistTrackDto(
            playlistTrack.getPlaylistId(),
            playlistTrack.getTrackId()
        );
    }
}
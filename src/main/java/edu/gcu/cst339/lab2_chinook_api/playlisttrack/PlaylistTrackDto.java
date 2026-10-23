package edu.gcu.cst339.lab2_chinook_api.playlisttrack;

import jakarta.validation.constraints.NotNull;   // field must not be null

/**
 * Data transfer object used as the JSON request and response body for
 * playlist-track links.
 *
 * <p>Both IDs come from the client because together they are the primary key,
 * so both are required.</p>
 *
 * @param playlistId ID of the playlist; required and must reference an existing playlist
 * @param trackId    ID of the track on that playlist; required and must reference an existing track
 */
record PlaylistTrackDto(
    @NotNull Integer playlistId,   // which playlist
    @NotNull Integer trackId       // which track is on that playlist
) {
    /**
     * Converts a playlist-track entity into a DTO by copying out its two IDs.
     *
     * @param playlistTrack the entity to convert
     * @return a DTO containing the playlist ID and track ID
     */
    static PlaylistTrackDto fromEntity(PlaylistTrack playlistTrack) {
        return new PlaylistTrackDto(
            playlistTrack.getPlaylistId(),
            playlistTrack.getTrackId()
        );
    }
}
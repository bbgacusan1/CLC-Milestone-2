package edu.gcu.cst339.lab2_chinook_api.playlisttrack;      

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;   // makes the @NotNull rules on the DTO actually run

/**
 * REST controller that manages which tracks are on which playlists, at
 * {@code /api/playlist-tracks}.
 *
 * <p>Because the primary key is a pair, a single row is addressed by both IDs
 * in the URL: {@code /api/playlist-tracks/{playlistId}/{trackId}}. All work is
 * delegated to {@link PlaylistTrackService}; empty results from the service are
 * turned into HTTP 404, or HTTP 409 when creating a link that already exists.</p>
 */
@RestController                         
@RequestMapping("/api/playlist-tracks") 
class PlaylistTrackController {

    private final PlaylistTrackService playlistTrackService;

    /**
     * Creates the controller. Spring injects the service through this constructor.
     *
     * @param playlistTrackService the service that performs playlist-track operations
     */
    PlaylistTrackController(PlaylistTrackService playlistTrackService) {
        this.playlistTrackService = playlistTrackService;
    }

    
    /**
     * Handles {@code GET /api/playlist-tracks}.
     *
     * @return every playlist-track link (HTTP 200)
     */
    @GetMapping
    List<PlaylistTrackDto> findAll() {
        return playlistTrackService.getAllPlaylistTracks();
    }

    /**
     * Handles {@code GET /api/playlist-tracks/{playlistId}/{trackId}}. Both IDs are
     * in the URL because together they form the primary key.
     *
     * @param playlistId the playlist part of the key, from the URL
     * @param trackId    the track part of the key, from the URL
     * @return the link (HTTP 200), or HTTP 404 if that track is not on that playlist
     */
    @GetMapping("/{playlistId}/{trackId}")
    ResponseEntity<PlaylistTrackDto> findById(@PathVariable Integer playlistId,
                                              @PathVariable Integer trackId) {
        return playlistTrackService.getPlaylistTrackById(playlistId, trackId)
                .map(ResponseEntity::ok)                      // found: 200 + the DTO
                .orElse(ResponseEntity.notFound().build());   // empty: 404
    }

    /**
     * Handles {@code POST /api/playlist-tracks} by adding a track to a playlist.
     * Both IDs come from the validated JSON body.
     *
     * @param dto the playlist ID and track ID to link, read from the JSON body
     * @return the new link (HTTP 201 Created) with a {@code Location} header
     *         pointing to {@code /api/playlist-tracks/{playlistId}/{trackId}}, or
     *         HTTP 409 Conflict if that track is already on that playlist
     */
    @PostMapping
    ResponseEntity<PlaylistTrackDto> create(@Valid @RequestBody PlaylistTrackDto dto) {
        return playlistTrackService.createPlaylistTrack(dto)
                .map(created -> {
                    // Location header: where the new link can be fetched
                    URI location = URI.create("/api/playlist-tracks/"
                            + created.playlistId() + "/" + created.trackId());
                    return ResponseEntity.created(location).body(created);   // 201
                })
                .orElse(ResponseEntity.status(HttpStatus.CONFLICT).build()); // empty: 409
    }

    /**
     * Handles {@code PUT /api/playlist-tracks/{playlistId}/{trackId}} by moving a
     * link. The URL identifies the existing (old) pair and the body holds the new pair.
     *
     * @param playlistId the playlist part of the existing key, from the URL
     * @param trackId    the track part of the existing key, from the URL
     * @param dto        the new playlist ID and track ID, read from the JSON body
     * @return the moved link (HTTP 200), or HTTP 404 if the old pair does not exist
     */
    @PutMapping("/{playlistId}/{trackId}")
    ResponseEntity<PlaylistTrackDto> update(@PathVariable Integer playlistId,
                                            @PathVariable Integer trackId,
                                            @Valid @RequestBody PlaylistTrackDto dto) {
        return playlistTrackService.updatePlaylistTrack(playlistId, trackId, dto)
                .map(ResponseEntity::ok)                      // updated: 200 + the DTO
                .orElse(ResponseEntity.notFound().build());   // no such pair: 404
    }

    /**
     * Handles {@code DELETE /api/playlist-tracks/{playlistId}/{trackId}} by removing
     * a track from a playlist.
     *
     * @param playlistId the playlist part of the key, from the URL
     * @param trackId    the track part of the key, from the URL
     * @return HTTP 204 No Content if the link was deleted, or HTTP 404 if that pair
     *         does not exist
     */
    @DeleteMapping("/{playlistId}/{trackId}")
    ResponseEntity<Void> delete(@PathVariable Integer playlistId,
                                @PathVariable Integer trackId) {
        if (playlistTrackService.deletePlaylistTrack(playlistId, trackId)) {
            return ResponseEntity.noContent().build();        // deleted: 204, empty body
        }
        return ResponseEntity.notFound().build();             // nothing to delete: 404
    }
}
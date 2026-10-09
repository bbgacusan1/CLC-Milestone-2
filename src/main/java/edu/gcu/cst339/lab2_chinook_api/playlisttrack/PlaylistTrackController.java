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

@RestController                         
@RequestMapping("/api/playlist-tracks") 
class PlaylistTrackController {

    private final PlaylistTrackService playlistTrackService;

    PlaylistTrackController(PlaylistTrackService playlistTrackService) {
        this.playlistTrackService = playlistTrackService;
    }

    
    @GetMapping
    List<PlaylistTrackDto> findAll() {
        return playlistTrackService.getAllPlaylistTracks();
    }

    // GET /api/playlist-tracks/{playlistId}/{trackId} -> 200 + the link, or 404 if that pair doesn't exist
    // Both ids are in the URL because the primary key is the pair.
    @GetMapping("/{playlistId}/{trackId}")
    ResponseEntity<PlaylistTrackDto> findById(@PathVariable Integer playlistId,
                                              @PathVariable Integer trackId) {
        return playlistTrackService.getPlaylistTrackById(playlistId, trackId)
                .map(ResponseEntity::ok)                      // found: 200 + the DTO
                .orElse(ResponseEntity.notFound().build());   // empty: 404
    }

    // POST /api/playlist-tracks -> 201 + the new link, or 409 if that track is already on that playlist
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

    // PUT /api/playlist-tracks/{playlistId}/{trackId} -> 200 + the moved link, or 404 if the old pair doesn't exist
    // The URL is the OLD pair (which row to change); the body is the NEW pair.
    @PutMapping("/{playlistId}/{trackId}")
    ResponseEntity<PlaylistTrackDto> update(@PathVariable Integer playlistId,
                                            @PathVariable Integer trackId,
                                            @Valid @RequestBody PlaylistTrackDto dto) {
        return playlistTrackService.updatePlaylistTrack(playlistId, trackId, dto)
                .map(ResponseEntity::ok)                      // updated: 200 + the DTO
                .orElse(ResponseEntity.notFound().build());   // no such pair: 404
    }

    // DELETE /api/playlist-tracks/{playlistId}/{trackId} -> 204 if deleted, or 404 if that pair doesn't exist
    @DeleteMapping("/{playlistId}/{trackId}")
    ResponseEntity<Void> delete(@PathVariable Integer playlistId,
                                @PathVariable Integer trackId) {
        if (playlistTrackService.deletePlaylistTrack(playlistId, trackId)) {
            return ResponseEntity.noContent().build();        // deleted: 204, empty body
        }
        return ResponseEntity.notFound().build();             // nothing to delete: 404
    }
}
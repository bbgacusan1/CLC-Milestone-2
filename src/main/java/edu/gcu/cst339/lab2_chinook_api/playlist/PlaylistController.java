package edu.gcu.cst339.lab2_chinook_api.playlist;

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

@RestController                 // handles HTTP requests; returned objects become JSON
@RequestMapping("/playlists")      // every endpoint below starts with /playlists
public class PlaylistController {

    // The controller HAS a service and only talks to it. It never sees the Playlist entity.
    private final PlaylistService playlistService;

    // Constructor injection: Spring passes in the service
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    /**
     * Retrieves every playlist.
     *
     * @return a list of all playlists as DTOs (HTTP 200)
     */
    @GetMapping
    public List<PlaylistDto> getAllPlaylists() {
        return playlistService.getAllPlaylists();
    }

    /**
     * Retrieves a single playlist by its id.
     *
     * @param id the playlist's primary key, taken from the URL
     * @return the playlist (HTTP 200), or HTTP 404 if no playlist has that id
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDto> getPlaylistById(@PathVariable Integer id) {
        return playlistService.getPlaylistById(id)
                .map(ResponseEntity::ok)                       // found: 200 + the DTO
                .orElse(ResponseEntity.notFound().build());    // empty: 404
    }

    /**
     * Creates a new playlist. The database assigns the id.
     *
     * @param playlistDto the name of the new playlist, read from the JSON body
     * @return the created playlist including its new id (HTTP 201)
     */
    @PostMapping
    public ResponseEntity<PlaylistDto> createPlaylist(@RequestBody PlaylistDto playlistDto) {
        PlaylistDto created = playlistService.createPlaylist(playlistDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Updates an existing playlist's name.
     *
     * @param id          the id of the playlist to update, taken from the URL
     * @param playlistDto the new values, read from the JSON body
     * @return the updated playlist (HTTP 200), or HTTP 404 if no playlist has that id
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlaylistDto> updatePlaylist(@PathVariable Integer id,
                                                     @RequestBody PlaylistDto playlistDto) {
        return playlistService.updatePlaylist(id, playlistDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes an existing playlist.
     *
     * @param id the id of the playlist to delete, taken from the URL
     * @return HTTP 204 if deleted, or HTTP 404 if no playlist has that id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Integer id) {
        if (playlistService.deletePlaylist(id)) {
            return ResponseEntity.noContent().build();         // deleted: 204, empty body
        }
        return ResponseEntity.notFound().build();              // nothing to delete: 404
    }
}
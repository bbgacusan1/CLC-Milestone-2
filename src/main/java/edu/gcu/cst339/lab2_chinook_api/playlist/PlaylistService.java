package edu.gcu.cst339.lab2_chinook_api.playlist;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Business logic for Chinook playlists.
 *
 * <p>Converts between playlist entities and DTOs. Instead of throwing an
 * exception for an unknown ID, the read and update methods return an empty
 * {@code Optional} and the delete method returns {@code false}; the controller
 * turns those results into HTTP 404.</p>
 */
@Service  // tells Spring: "create one of these and manage it" (a Spring bean)
public class PlaylistService {

    // The service HAS a repository and calls it. It does not implement it.
    private final PlaylistRepository playlistRepository;

    /**
     * Creates the service. Spring injects the generated repository through this constructor.
     *
     * @param playlistRepository the repository used to read and write playlists
     */
    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    /**
     * Returns every playlist in the database.
     *
     * @return all playlists as DTOs; an empty list if the table is empty
     */
    public List<PlaylistDto> getAllPlaylists() {
        return playlistRepository.findAll().stream()
                .map(PlaylistDto::fromEntity)      // run toDto() on each Playlist  
                .toList();
    }

    /**
     * Looks up a playlist by its ID.
     *
     * @param id the playlist ID to look up
     * @return the matching playlist, or an empty {@code Optional} if no playlist has that ID
     */
    public Optional<PlaylistDto> getPlaylistById(Integer id) {
        return playlistRepository.findById(id).map(PlaylistDto::fromEntity);
    }

    /**
     * Saves a new playlist. Any {@code playlistId} in the DTO is ignored because
     * the database generates the ID.
     *
     * @param dto the name of the new playlist
     * @return the saved playlist, including its generated ID
     */
    public PlaylistDto createPlaylist(PlaylistDto dto) {
        Playlist playlist = new Playlist();
        playlist.setName(dto.name());          // dto.name(), not getName(): it's a record
        return PlaylistDto.fromEntity(playlistRepository.save(playlist));
    }

    /**
     * Replaces the name of an existing playlist and saves it.
     *
     * @param id  the ID of the playlist to update
     * @param dto the new name; any {@code playlistId} in the DTO is ignored
     * @return the updated playlist, or an empty {@code Optional} if no playlist has that ID
     */
    public Optional<PlaylistDto> updatePlaylist(Integer id, PlaylistDto dto) {
        return playlistRepository.findById(id).map(playlist -> {
            playlist.setName(dto.name());
            return PlaylistDto.fromEntity(playlistRepository.save(playlist));
        });
    }

    /**
     * Deletes the playlist with the given ID.
     *
     * @param id the ID of the playlist to delete
     * @return {@code true} if the playlist was deleted, or {@code false} if no
     *         playlist has that ID
     */
    public boolean deletePlaylist(Integer id) {
        if (!playlistRepository.existsById(id)) {
            return false;
        }
        playlistRepository.deleteById(id);
        return true;
    }

 
}

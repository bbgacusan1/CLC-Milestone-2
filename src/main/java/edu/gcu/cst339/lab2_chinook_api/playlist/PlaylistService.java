package edu.gcu.cst339.lab2_chinook_api.playlist;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service  // tells Spring: "create one of these and manage it" (a Spring bean)
public class PlaylistService {

    // The service HAS a repository and calls it. It does not implement it.
    private final PlaylistRepository playlistRepository;

    // Constructor injection: Spring passes in the generated repository
    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    // READ ALL: get every Playlist entity, convert each one to a DTO
    public List<PlaylistDto> getAllPlaylists() {
        return playlistRepository.findAll().stream()
                .map(this::toDto)      // run toDto() on each Playlist  
                .toList();
    }

    // READ ONE: findById returns Optional<Playlist> (might be empty); convert to a DTO if present
    public Optional<PlaylistDto> getPlaylistById(Integer id) {
        return playlistRepository.findById(id).map(this::toDto);
    }

    // CREATE: DTO -> new entity -> save -> saved entity -> DTO
    public PlaylistDto createPlaylist(PlaylistDto dto) {
        Playlist playlist = new Playlist();
        playlist.setName(dto.name());          // dto.name(), not getName(): it's a record
        return toDto(playlistRepository.save(playlist));
    }

    // UPDATE: load the existing playlist, change its fields, save. Empty if it doesn't exist.
    public Optional<PlaylistDto> updatePlaylist(Integer id, PlaylistDto dto) {
        return playlistRepository.findById(id).map(playlist -> {
            playlist.setName(dto.name());
            return toDto(playlistRepository.save(playlist));
        });
    }

    // DELETE: true if something was deleted, false if that id didn't exist
    public boolean deletePlaylist(Integer id) {
        if (!playlistRepository.existsById(id)) {
            return false;
        }
        playlistRepository.deleteById(id);
        return true;
    }

    // Entity -> DTO. The entity never leaves this class.
    private PlaylistDto toDto(Playlist playlist) {
        return new PlaylistDto(playlist.getPlaylistId(), playlist.getName());
    }
}

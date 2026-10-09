package edu.gcu.cst339.lab2_chinook_api.playlisttrack;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.gcu.cst339.lab2_chinook_api.playlisttrack.PlaylistTrack.PlaylistTrackId;

@Service  
public class PlaylistTrackService {

    private final PlaylistTrackRepository playlistTrackRepository;

    public PlaylistTrackService(PlaylistTrackRepository playlistTrackRepository) {
        this.playlistTrackRepository = playlistTrackRepository;
    }

    // READ ALL: get every PlaylistTrack entity, convert each one to a DTO
    public List<PlaylistTrackDto> getAllPlaylistTracks() {
        return playlistTrackRepository.findAll().stream()
                .map(PlaylistTrackDto::fromEntity)
                .toList();
    }

    // READ ONE: build the key "stub" from both ids, then look it up
    public Optional<PlaylistTrackDto> getPlaylistTrackById(Integer playlistId, Integer trackId) {
        return playlistTrackRepository.findById(new PlaylistTrackId(playlistId, trackId))
                .map(PlaylistTrackDto::fromEntity);
    }

    // CREATE: both ids come from the client. Empty result means that link already exists.
    public Optional<PlaylistTrackDto> createPlaylistTrack(PlaylistTrackDto dto) {
        PlaylistTrackId key = new PlaylistTrackId(dto.playlistId(), dto.trackId());
        if (playlistTrackRepository.existsById(key)) {
            return Optional.empty();   // already on the playlist: the controller turns this into a 409
        }
        PlaylistTrack playlistTrack = new PlaylistTrack();
        playlistTrack.setPlaylistId(dto.playlistId());   // no copyFields helper: only two fields
        playlistTrack.setTrackId(dto.trackId());
        return Optional.of(PlaylistTrackDto.fromEntity(playlistTrackRepository.save(playlistTrack)));
    }

    // UPDATE: move the link at (playlistId, trackId) to the pair in the DTO.
    // @Transactional makes the delete + insert succeed or fail together.
    @Transactional
    public Optional<PlaylistTrackDto> updatePlaylistTrack(Integer playlistId, Integer trackId,
                                                          PlaylistTrackDto dto) {
        return playlistTrackRepository.findById(new PlaylistTrackId(playlistId, trackId))
                .map(existing -> {
                    // Same pair as before: nothing to change
                    if (dto.playlistId().equals(playlistId) && dto.trackId().equals(trackId)) {
                        return PlaylistTrackDto.fromEntity(existing);
                    }
                    // A primary key can't be edited, so remove the old row and add the new one
                    playlistTrackRepository.delete(existing);
                    PlaylistTrack moved = new PlaylistTrack();
                    moved.setPlaylistId(dto.playlistId());
                    moved.setTrackId(dto.trackId());
                    return PlaylistTrackDto.fromEntity(playlistTrackRepository.save(moved));
                });
    }

    // DELETE: true if something was deleted, false if that pair didn't exist
    public boolean deletePlaylistTrack(Integer playlistId, Integer trackId) {
        PlaylistTrackId key = new PlaylistTrackId(playlistId, trackId);
        if (!playlistTrackRepository.existsById(key)) {
            return false;
        }
        playlistTrackRepository.deleteById(key);
        return true;
    }
}
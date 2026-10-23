package edu.gcu.cst339.lab2_chinook_api.playlisttrack;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.gcu.cst339.lab2_chinook_api.playlisttrack.PlaylistTrack.PlaylistTrackId;

/**
 * Business logic for the playlist-track join table.
 *
 * <p>Every lookup builds a composite key from the playlist ID and track ID.
 * Instead of throwing exceptions, methods return an empty {@code Optional} or
 * {@code false} when a row is missing (or, for create, already exists); the
 * controller turns those results into HTTP status codes.</p>
 */
@Service  
public class PlaylistTrackService {

    private final PlaylistTrackRepository playlistTrackRepository;

    /**
     * Creates the service. Spring injects the repository through this constructor.
     *
     * @param playlistTrackRepository the repository used to read and write playlist-track rows
     */
    public PlaylistTrackService(PlaylistTrackRepository playlistTrackRepository) {
        this.playlistTrackRepository = playlistTrackRepository;
    }

    /**
     * Returns every playlist-track link in the database.
     *
     * @return all links as DTOs; an empty list if the table is empty
     */
    public List<PlaylistTrackDto> getAllPlaylistTracks() {
        return playlistTrackRepository.findAll().stream()
                .map(PlaylistTrackDto::fromEntity)
                .toList();
    }

    /**
     * Looks up a single link by building the composite key from both IDs.
     *
     * @param playlistId the playlist part of the key
     * @param trackId    the track part of the key
     * @return the matching link, or an empty {@code Optional} if that track is not on that playlist
     */
    public Optional<PlaylistTrackDto> getPlaylistTrackById(Integer playlistId, Integer trackId) {
        return playlistTrackRepository.findById(new PlaylistTrackId(playlistId, trackId))
                .map(PlaylistTrackDto::fromEntity);
    }

    /**
     * Adds a track to a playlist.
     *
     * <p>If either ID does not reference an existing row, the database rejects the
     * insert with a foreign-key violation.</p>
     *
     * @param dto the playlist ID and track ID to link
     * @return the saved link, or an empty {@code Optional} if that link already exists
     */
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

    /**
     * Moves the link at ({@code playlistId}, {@code trackId}) to the pair in the DTO.
     *
     * <p>A primary key cannot be edited in place, so the old row is deleted and a
     * new row is inserted. {@code @Transactional} makes the delete and insert
     * succeed or fail together. If the new pair equals the old pair, nothing changes.</p>
     *
     * @param playlistId the playlist part of the existing key
     * @param trackId    the track part of the existing key
     * @param dto        the new playlist ID and track ID
     * @return the moved link, or an empty {@code Optional} if the old pair does not exist
     */
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

    /**
     * Removes a track from a playlist.
     *
     * @param playlistId the playlist part of the key
     * @param trackId    the track part of the key
     * @return {@code true} if the link was deleted, or {@code false} if that pair does not exist
     */
    public boolean deletePlaylistTrack(Integer playlistId, Integer trackId) {
        PlaylistTrackId key = new PlaylistTrackId(playlistId, trackId);
        if (!playlistTrackRepository.existsById(key)) {
            return false;
        }
        playlistTrackRepository.deleteById(key);
        return true;
    }
}
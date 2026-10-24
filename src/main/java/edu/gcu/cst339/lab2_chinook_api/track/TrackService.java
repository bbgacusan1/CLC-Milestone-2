package edu.gcu.cst339.lab2_chinook_api.track;

import java.util.List; 
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Business logic for Chinook tracks.
 *
 * <p>Converts between track entities and DTOs. Instead of throwing an
 * exception for an unknown ID, the read and update methods return an empty
 * {@code Optional} and the delete method returns {@code false}; the controller
 * turns those results into HTTP 404.</p>
 */
@Service  // tells Spring: "create one of these and manage it" (a Spring bean)
public class TrackService {

    // The service HAS a repository and calls it. It does not implement it.
    private final TrackRepository trackRepository;

    /**
     * Creates the service. Spring injects the generated repository through this constructor.
     *
     * @param trackRepository the repository used to read and write tracks
     */
    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    /**
     * Returns every track in the database.
     *
     * @return all tracks as DTOs; an empty list if the table is empty
     */
    public List<TrackDto> getAllTracks() {
        return trackRepository.findAll().stream()
                .map(TrackDto::fromEntity)      // run toDto() on each Track  
                .toList();
    }

    /**
     * Looks up a track by its ID.
     *
     * @param id the track ID to look up
     * @return the matching track, or an empty {@code Optional} if no track has that ID
     */
    public Optional<TrackDto> getTrackById(Integer id) {
        return trackRepository.findById(id).map(TrackDto::fromEntity);
    }

    /**
     * Saves a new track. Any {@code trackId} in the DTO is ignored because the
     * database generates the ID.
     *
     * @param dto the details of the new track
     * @return the saved track, including its generated ID
     */
    public TrackDto createTrack(TrackDto dto) {
        Track track = new Track();
        copyFields(dto, track);        
        return TrackDto.fromEntity(trackRepository.save(track));
    }

    /**
     * Replaces every editable field of an existing track and saves it.
     *
     * @param id  the ID of the track to update
     * @param dto the new values; any {@code trackId} in the DTO is ignored
     * @return the updated track, or an empty {@code Optional} if no track has that ID
     */
    public Optional<TrackDto> updateTrack(Integer id, TrackDto dto) {
        return trackRepository.findById(id).map(track -> {
            copyFields(dto, track); 
            return TrackDto.fromEntity(trackRepository.save(track));
        });
    }

    /**
     * Deletes the track with the given ID.
     *
     * @param id the ID of the track to delete
     * @return {@code true} if the track was deleted, or {@code false} if no track has that ID
     */
    public boolean deleteTrack(Integer id) {
        if (!trackRepository.existsById(id)) {
            return false;
        }
        trackRepository.deleteById(id);
        return true;
    }

    // Copies every editable field from the DTO onto the entity.
    // trackId is skipped because the database generates it.
    private void copyFields(TrackDto dto, Track track) {
        track.setName(dto.name());
        track.setAlbumId(dto.albumId());
        track.setMediaTypeId(dto.mediaTypeId());
        track.setGenreId(dto.genreId());
        track.setComposer(dto.composer());
        track.setMilliseconds(dto.milliseconds());
        track.setBytes(dto.bytes());
        track.setUnitPrice(dto.unitPrice());
    }
}

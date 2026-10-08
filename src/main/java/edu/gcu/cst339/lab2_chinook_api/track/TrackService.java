package edu.gcu.cst339.lab2_chinook_api.track;

import java.util.List; 
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service  // tells Spring: "create one of these and manage it" (a Spring bean)
public class TrackService {

    // The service HAS a repository and calls it. It does not implement it.
    private final TrackRepository trackRepository;

    // Constructor injection: Spring passes in the generated repository
    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    // READ ALL: get every Track entity, convert each one to a DTO
    public List<TrackDto> getAllTracks() {
        return trackRepository.findAll().stream()
                .map(TrackDto::fromEntity)      // run toDto() on each Track  
                .toList();
    }

    // READ ONE: findById returns Optional<Track> (might be empty); convert to a DTO if present
    public Optional<TrackDto> getTrackById(Integer id) {
        return trackRepository.findById(id).map(TrackDto::fromEntity);
    }

    // CREATE: DTO -> new entity -> save -> saved entity -> DTO
    public TrackDto createTrack(TrackDto dto) {
        Track track = new Track();
        copyFields(dto, track);        
        return TrackDto.fromEntity(trackRepository.save(track));
    }

    // UPDATE: load the existing track, change its fields, save. Empty if it doesn't exist.
    public Optional<TrackDto> updateTrack(Integer id, TrackDto dto) {
        return trackRepository.findById(id).map(track -> {
            copyFields(dto, track); 
            return TrackDto.fromEntity(trackRepository.save(track));
        });
    }

    // DELETE: true if something was deleted, false if that id didn't exist
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

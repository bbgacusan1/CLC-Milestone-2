package edu.gcu.cst339.lab2_chinook_api.track;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tracks")
class TrackController {

    private final TrackService trackService;

    TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping
    List<TrackDto> findAll() {
        return trackService.getAllTracks();
    }

    // GET /api/tracks/{id} -> 200 + the track, or 404 if no track has that id
    @GetMapping("/{id}")
    ResponseEntity<TrackDto> findById(@PathVariable Integer id) {
        return trackService.getTrackById(id)
                .map(ResponseEntity::ok)                      // found: 200 + the DTO
                .orElse(ResponseEntity.notFound().build());   // empty: 404
    }

    @PostMapping
    ResponseEntity<TrackDto> create(@Valid @RequestBody TrackDto dto) {
        TrackDto created = trackService.createTrack(dto);
        URI location = URI.create("/api/tracks/" + created.trackId());
        return ResponseEntity.created(location).body(created);
    }
 
    // PUT /api/tracks/{id} -> 200 + the updated track, or 404 if no track has that id
    @PutMapping("/{id}")
    ResponseEntity<TrackDto> update(@PathVariable Integer id, @Valid @RequestBody TrackDto dto) {
        return trackService.updateTrack(id, dto)
                .map(ResponseEntity::ok)                      // updated: 200 + the DTO
                .orElse(ResponseEntity.notFound().build());   // no such track: 404
    }

    // DELETE /api/tracks/{id} -> 204 if deleted, or 404 if no track has that id
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (trackService.deleteTrack(id)) {
            return ResponseEntity.noContent().build();        // deleted: 204, empty body
        }
        return ResponseEntity.notFound().build();             // nothing to delete: 404
    }
}
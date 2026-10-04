package edu.gcu.cst339.lab2_chinook_api.artist;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

class ArtistController {
    private final ArtistService artistService;

    ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    List<ArtistDto> findAll() {
        return artistService.findAll();
    }

    @GetMapping("/{id}")
    ArtistDto findById(Integer id) {
        return artistService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ArtistDto create(@RequestBody ArtistDto dto) {
        return artistService.create(dto);
    }

    @PutMapping("/{id}")
    ResponseEntity<ArtistDto> update(@PathVariable Integer id, @RequestBody ArtistDto dto) {
        ArtistDto updated = artistService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Integer id) {
        artistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

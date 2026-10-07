package edu.gcu.cst339.lab2_chinook_api.genre;

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
@RequestMapping("/api/genres")
class GenreController {

    private final GenreService genreService;

    GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    List<GenreDto> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    GenreDto findById(@PathVariable Integer id) {
        return genreService.findById(id);
    }

    @PostMapping
    ResponseEntity<GenreDto> create(@Valid @RequestBody GenreDto dto) {
        GenreDto created = genreService.create(dto);
        URI location = URI.create("/api/genres/" + created.genreId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    ResponseEntity<GenreDto> update(@PathVariable Integer id, @Valid @RequestBody GenreDto dto) {
        return ResponseEntity.ok(genreService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Integer id) {
        genreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
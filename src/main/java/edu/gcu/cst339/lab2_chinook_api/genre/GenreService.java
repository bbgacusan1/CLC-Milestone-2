package edu.gcu.cst339.lab2_chinook_api.genre;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class GenreService {

    private final GenreRepository genreRepository;

    GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Transactional(readOnly = true)
    List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(GenreDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    GenreDto findById(Integer id) {
        return genreRepository.findById(id)
                .map(GenreDto::fromEntity)
                .orElseThrow(() -> new GenreNotFoundException(id));
    }

    @Transactional
    GenreDto create(GenreDto dto) {
        Genre genre = new Genre();
        genre.setName(dto.name());
        return GenreDto.fromEntity(genreRepository.save(genre));
    }

    @Transactional
    GenreDto update(Integer id, GenreDto dto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException(id));
        genre.setName(dto.name());
        return GenreDto.fromEntity(genre);
    }

    @Transactional
    void delete(Integer id) {
        if (!genreRepository.existsById(id)) {
            throw new GenreNotFoundException(id);
        }
        genreRepository.deleteById(id);
    }
}
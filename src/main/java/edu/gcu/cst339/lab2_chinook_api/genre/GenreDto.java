package edu.gcu.cst339.lab2_chinook_api.genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenreDto(
        Integer genreId,
        @NotBlank @Size(max = 120) String name) {

    static GenreDto fromEntity(Genre genre) {
        return new GenreDto(genre.getGenreId(), genre.getName());
    }
}
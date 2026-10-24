package edu.gcu.cst339.lab2_chinook_api.genre;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for Chinook genres.
 *
 * <p>Converts between {@link Genre} entities and {@link GenreDto} objects,
 * throws {@link GenreNotFoundException} for unknown IDs, and runs each
 * operation in a transaction.</p>
 */
@Service
class GenreService {

    private final GenreRepository genreRepository;

    /**
     * Creates the service. Spring injects the repository through this constructor.
     *
     * @param genreRepository the repository used to read and write genres
     */
    GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    /**
     * Returns every genre in the database.
     *
     * @return all genres as DTOs; an empty list if the table is empty
     */
    @Transactional(readOnly = true)
    List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(GenreDto::fromEntity)
                .toList();
    }

    /**
     * Returns the genre with the given ID.
     *
     * @param id the genre ID to look up
     * @return the matching genre as a DTO
     * @throws GenreNotFoundException if no genre has the given ID
     */
    @Transactional(readOnly = true)
    GenreDto findById(Integer id) {
        return genreRepository.findById(id)
                .map(GenreDto::fromEntity)
                .orElseThrow(() -> new GenreNotFoundException(id));
    }

    /**
     * Saves a new genre. Any {@code genreId} in the DTO is ignored because the
     * database generates the ID.
     *
     * @param dto the name of the new genre
     * @return the saved genre, including its generated ID
     */
    @Transactional
    GenreDto create(GenreDto dto) {
        Genre genre = new Genre();
        genre.setName(dto.name());
        return GenreDto.fromEntity(genreRepository.save(genre));
    }

    /**
     * Replaces the name of an existing genre.
     *
     * <p>The entity is changed inside the transaction and JPA writes the change
     * when the transaction commits, so no explicit {@code save} call is needed.</p>
     *
     * @param id  the ID of the genre to update
     * @param dto the new name; any {@code genreId} in the DTO is ignored
     * @return the updated genre
     * @throws GenreNotFoundException if no genre has the given ID
     */
    @Transactional
    GenreDto update(Integer id, GenreDto dto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException(id));
        genre.setName(dto.name());
        return GenreDto.fromEntity(genre);
    }

    /**
     * Deletes the genre with the given ID.
     *
     * @param id the ID of the genre to delete
     * @throws GenreNotFoundException if no genre has the given ID
     */
    @Transactional
    void delete(Integer id) {
        if (!genreRepository.existsById(id)) {
            throw new GenreNotFoundException(id);
        }
        genreRepository.deleteById(id);
    }
}
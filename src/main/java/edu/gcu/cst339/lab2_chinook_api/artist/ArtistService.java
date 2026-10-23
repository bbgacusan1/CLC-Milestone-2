package edu.gcu.cst339.lab2_chinook_api.artist;

import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for Chinook artists.
 *
 * <p>Converts between {@link Artist} entities and {@link ArtistDto} objects,
 * throws {@link ArtistNotFoundException} for unknown IDs, and runs each
 * operation in a transaction.</p>
 */
@Service
class ArtistService {
    private final ArtistRepository artistRepository; 

    /**
     * Creates the service. Spring injects the repository through this constructor.
     *
     * @param artistRepository the repository used to read and write artists
     */
    ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }
    
    /**
     * Returns every artist in the database.
     *
     * @return all artists as DTOs; an empty list if the table is empty
     */
    @Transactional(readOnly = true)
    List<ArtistDto> findAll() {
        return artistRepository.findAll().stream()
            .map(ArtistDto::fromEntity)
            .toList();
    }

    /**
     * Returns the artist with the given ID.
     *
     * @param id the artist ID to look up
     * @return the matching artist as a DTO
     * @throws ArtistNotFoundException if no artist has the given ID
     */
    @Transactional(readOnly = true)
    ArtistDto findById(Integer id) {
        return artistRepository.findById(id)
            .map(ArtistDto::fromEntity)
            .orElseThrow(() -> new ArtistNotFoundException(id));
    }

    /**
     * Saves a new artist. Any {@code artistId} in the DTO is ignored because the
     * database generates the ID.
     *
     * @param dto the name of the new artist
     * @return the saved artist, including its generated ID
     */
    @Transactional
    ArtistDto create(ArtistDto dto) {
        Artist saved = artistRepository.save(new Artist(dto.name()));
        return ArtistDto.fromEntity(saved);
    }

    /**
     * Replaces the name of an existing artist.
     *
     * <p>The entity is changed inside the transaction and JPA writes the change
     * when the transaction commits, so no explicit {@code save} call is needed.</p>
     *
     * @param id  the ID of the artist to update
     * @param dto the new name; any {@code artistId} in the DTO is ignored
     * @return the updated artist
     * @throws ArtistNotFoundException if no artist has the given ID
     */
    @Transactional
    ArtistDto update(Integer id, ArtistDto dto) {
        Artist artist = artistRepository.findById(id)
            .orElseThrow(() -> new ArtistNotFoundException(id));
        artist.setName(dto.name());
        return ArtistDto.fromEntity(artist);
    }

    /**
     * Deletes the artist with the given ID.
     *
     * @param id the ID of the artist to delete
     * @throws ArtistNotFoundException if no artist has the given ID
     */
    @Transactional
    void delete(Integer id) {
        if (!artistRepository.existsById(id)) {
            throw new ArtistNotFoundException(id);
        }
        artistRepository.deleteById(id);
    }
}

package edu.gcu.cst339.lab2_chinook_api.album;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for Chinook albums.
 *
 * <p>Sits between {@link AlbumController} and {@link AlbumRepository}: it converts
 * between entities and {@link AlbumDto} objects, throws {@link AlbumNotFoundException}
 * when an ID does not exist, and runs every operation inside a transaction.
 * Creates, updates and deletes are logged at INFO level.</p>
 */
@Service
class AlbumService {
    
    private static final Logger log = LoggerFactory.getLogger(AlbumService.class);
    
    private final AlbumRepository albumRepository;

    /**
     * Creates the service. Spring injects the repository through this constructor.
     *
     * @param albumRepository the repository used to read and write albums
     */
    AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    /**
     * Returns every album in the database.
     *
     * @return all albums as DTOs; an empty list if the table is empty
     */
    @Transactional(readOnly = true)
    List<AlbumDto> findAll() {
        log.debug("Finding all albums");
        return albumRepository.findAll().stream()
            .map(AlbumDto::fromEntity)
            .toList();
    }

    /**
     * Returns the album with the given ID.
     *
     * @param id the album ID to look up
     * @return the matching album as a DTO
     * @throws AlbumNotFoundException if no album has the given ID
     */
    @Transactional(readOnly = true)
    AlbumDto findById(Integer id) {
        log.debug("Finding album {}", id);
        return albumRepository.findById(id)
            .map(AlbumDto::fromEntity)
            .orElseThrow(() -> new AlbumNotFoundException(id));
    }

    /**
     * Saves a new album. Any {@code albumId} in the DTO is ignored because the
     * database generates the ID.
     *
     * @param dto the title and artist ID of the new album
     * @return the saved album, including its generated ID
     */
    @Transactional
    AlbumDto create(AlbumDto dto) {
        Album saved = albumRepository.save(new Album(dto.title(), dto.artistId()));
        log.info("Created album {}", saved.getAlbumId());
        return AlbumDto.fromEntity(saved);
    }

    /**
     * Replaces the title and artist of an existing album.
     *
     * @param id  the ID of the album to update
     * @param dto the new title and artist ID; any {@code albumId} in the DTO is ignored
     * @return the updated album
     * @throws AlbumNotFoundException if no album has the given ID
     */
    @Transactional
    AlbumDto update(Integer id, AlbumDto dto) {
        Album album = albumRepository.findById(id)
            .orElseThrow(() -> new AlbumNotFoundException(id));
        album.setTitle(dto.title());
        album.setArtistId(dto.artistId());
        log.info("Updated album {}", id);
        return AlbumDto.fromEntity(albumRepository.save(album));
    }

    /**
     * Deletes the album with the given ID.
     *
     * <p>The database rejects the delete if tracks still belong to the album; the
     * controller reports that case as HTTP 409 Conflict.</p>
     *
     * @param id the ID of the album to delete
     * @throws AlbumNotFoundException if no album has the given ID
     */
    @Transactional
    void delete(Integer id) {
        if (!albumRepository.existsById(id)) {
            throw new AlbumNotFoundException(id);
        }
        albumRepository.deleteById(id);
        log.info("Deleted album {}", id);
    }
}

package edu.gcu.cst339.lab2_chinook_api.mediatype;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for Chinook media types.
 *
 * <p>Converts between {@link MediaType} entities and {@link MediaTypeDto}
 * objects, throws {@link MediaTypeNotFoundException} for unknown IDs, and runs
 * each operation in a transaction.</p>
 */
@Service
class MediaTypeService {
    private final MediaTypeRepository mediaTypeRepository;

    /**
     * Creates the service. Spring injects the repository through this constructor.
     *
     * @param mediaTypeRepository the repository used to read and write media types
     */
    MediaTypeService(MediaTypeRepository mediaTypeRepository) {
        this.mediaTypeRepository = mediaTypeRepository;
    }

    /**
     * Returns every media type in the database.
     *
     * @return all media types as DTOs; an empty list if the table is empty
     */
    @Transactional(readOnly = true)
    List<MediaTypeDto> findAll() {
        return mediaTypeRepository.findAll().stream()
                .map(MediaTypeDto::fromEntity)
                .toList();
    }

    /**
     * Returns the media type with the given ID.
     *
     * @param id the media type ID to look up
     * @return the matching media type as a DTO
     * @throws MediaTypeNotFoundException if no media type has the given ID
     */
    @Transactional(readOnly = true)
    MediaTypeDto findById(Integer id) {
        return mediaTypeRepository.findById(id)
                .map(MediaTypeDto::fromEntity)
                .orElseThrow(() -> new MediaTypeNotFoundException(id));
    }

    /**
     * Saves a new media type. Any {@code mediaTypeId} in the DTO is ignored
     * because the database generates the ID.
     *
     * @param mediaTypeDto the name of the new media type
     * @return the saved media type, including its generated ID
     */
    @Transactional
    MediaTypeDto create(MediaTypeDto mediaTypeDto) {
        MediaType mediaType = new MediaType(mediaTypeDto.name());
        MediaType savedMediaType = mediaTypeRepository.save(mediaType);
        return MediaTypeDto.fromEntity(savedMediaType);
    }

    /**
     * Replaces the name of an existing media type and saves it.
     *
     * @param id           the ID of the media type to update
     * @param mediaTypeDto the new name; any {@code mediaTypeId} in the DTO is ignored
     * @return the updated media type
     * @throws MediaTypeNotFoundException if no media type has the given ID
     */
    @Transactional
    MediaTypeDto update(Integer id, MediaTypeDto mediaTypeDto) {
        MediaType mediaType = mediaTypeRepository.findById(id)
                .orElseThrow(() -> new MediaTypeNotFoundException(id));
        mediaType.setName(mediaTypeDto.name());
        MediaType savedMediaType = mediaTypeRepository.save(mediaType);
        return MediaTypeDto.fromEntity(savedMediaType);
    }

    /**
     * Deletes the media type with the given ID.
     *
     * <p>The database rejects the delete if tracks still use this media type;
     * the controller reports that case as HTTP 409 Conflict.</p>
     *
     * @param id the ID of the media type to delete
     * @throws MediaTypeNotFoundException if no media type has the given ID
     */
    @Transactional
    void delete(Integer id) {
        if (!mediaTypeRepository.existsById(id)) {
            throw new MediaTypeNotFoundException(id);
        }
        mediaTypeRepository.deleteById(id);
    }
}
package edu.gcu.cst339.lab2_chinook_api.mediatype;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class MediaTypeService {
    private final MediaTypeRepository mediaTypeRepository;

    MediaTypeService(MediaTypeRepository mediaTypeRepository) {
        this.mediaTypeRepository = mediaTypeRepository;
    }

    @Transactional(readOnly = true)
    List<MediaTypeDto> findAll() {
        return mediaTypeRepository.findAll().stream()
                .map(MediaTypeDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    MediaTypeDto findById(Integer id) {
        return mediaTypeRepository.findById(id)
                .map(MediaTypeDto::fromEntity)
                .orElseThrow(() -> new MediaTypeNotFoundException(id));
    }

    @Transactional
    MediaTypeDto create(MediaTypeDto mediaTypeDto) {
        MediaType mediaType = new MediaType(mediaTypeDto.name());
        MediaType savedMediaType = mediaTypeRepository.save(mediaType);
        return MediaTypeDto.fromEntity(savedMediaType);
    }

    @Transactional
    MediaTypeDto update(Integer id, MediaTypeDto mediaTypeDto) {
        MediaType mediaType = mediaTypeRepository.findById(id)
                .orElseThrow(() -> new MediaTypeNotFoundException(id));
        mediaType.setName(mediaTypeDto.name());
        MediaType savedMediaType = mediaTypeRepository.save(mediaType);
        return MediaTypeDto.fromEntity(savedMediaType);
    }

    @Transactional
    void delete(Integer id) {
        if (!mediaTypeRepository.existsById(id)) {
            throw new MediaTypeNotFoundException(id);
        }
        mediaTypeRepository.deleteById(id);
    }
}
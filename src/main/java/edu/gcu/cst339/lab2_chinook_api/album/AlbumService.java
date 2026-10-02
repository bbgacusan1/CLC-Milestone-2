package edu.gcu.cst339.lab2_chinook_api.album;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class AlbumService {
    
    private static final Logger log = LoggerFactory.getLogger(AlbumService.class);
    
    private final AlbumRepository albumRepository;

    AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Transactional(readOnly = true)
    List<AlbumDto> findAll() {
        log.debug("Finding all albums");
        return albumRepository.findAll().stream()
            .map(AlbumDto::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    AlbumDto findById(Integer id) {
        log.debug("Finding album {}", id);
        return albumRepository.findById(id)
            .map(AlbumDto::fromEntity)
            .orElseThrow(() -> new AlbumNotFoundException(id));
    }

    @Transactional
    AlbumDto create(AlbumDto dto) {
        Album saved = albumRepository.save(new Album(dto.title(), dto.artistId()));
        log.info("Created album {}", saved.getAlbumId());
        return AlbumDto.fromEntity(saved);
    }

    @Transactional
    AlbumDto update(Integer id, AlbumDto dto) {
        Album album = albumRepository.findById(id)
            .orElseThrow(() -> new AlbumNotFoundException(id));
        album.setTitle(dto.title());
        album.setArtistId(dto.artistId());
        log.info("Updated album {}", id);
        return AlbumDto.fromEntity(albumRepository.save(album));
    }

    @Transactional
    void delete(Integer id) {
        if (!albumRepository.existsById(id)) {
            throw new AlbumNotFoundException(id);
        }
        albumRepository.deleteById(id);
        log.info("Deleted album {}", id);
    }
}

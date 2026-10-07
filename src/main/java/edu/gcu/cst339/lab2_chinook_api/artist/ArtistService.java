package edu.gcu.cst339.lab2_chinook_api.artist;

import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
class ArtistService {
    private final ArtistRepository artistRepository; 

    ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }
    
    @Transactional(readOnly = true)
    List<ArtistDto> findAll() {
        return artistRepository.findAll().stream()
            .map(ArtistDto::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    ArtistDto findById(Integer id) {
        return artistRepository.findById(id)
            .map(ArtistDto::fromEntity)
            .orElseThrow(() -> new ArtistNotFoundException(id));
    }

    @Transactional
    ArtistDto create(ArtistDto dto) {
        Artist saved = artistRepository.save(new Artist(dto.name()));
        return ArtistDto.fromEntity(saved);
    }

    @Transactional
    ArtistDto update(Integer id, ArtistDto dto) {
        Artist artist = artistRepository.findById(id)
            .orElseThrow(() -> new ArtistNotFoundException(id));
        artist.setName(dto.name());
        return ArtistDto.fromEntity(artist);
    }

    @Transactional
    void delete(Integer id) {
        if (!artistRepository.existsById(id)) {
            throw new ArtistNotFoundException(id);
        }
        artistRepository.deleteById(id);
    }
}

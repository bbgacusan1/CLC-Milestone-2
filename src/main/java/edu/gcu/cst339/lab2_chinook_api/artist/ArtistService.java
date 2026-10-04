package edu.gcu.cst339.lab2_chinook_api.artist;

import org.springframework.stereotype.Service;

@Service
class ArtistService {
    private final ArtistRepository artistRepository; 

    ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

}

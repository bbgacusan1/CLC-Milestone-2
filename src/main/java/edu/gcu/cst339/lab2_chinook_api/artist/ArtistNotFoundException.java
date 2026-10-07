package edu.gcu.cst339.lab2_chinook_api.artist;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ArtistNotFoundException
 */
@ResponseStatus (HttpStatus.NOT_FOUND)
public class ArtistNotFoundException extends RuntimeException{
    ArtistNotFoundException(Integer id) {
        super("Artist not found with ID: " + id);
    }
}

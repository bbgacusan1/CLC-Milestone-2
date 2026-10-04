package edu.gcu.cst339.lab2_chinook_api.artist;

/**
 * ArtistNotFoundException
 */
public class ArtistNotFoundException extends RuntimeException{
    ArtistNotFoundException(Integer id) {
        super("Artist not found with ID: " + id);
    }
}

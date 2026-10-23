package edu.gcu.cst339.lab2_chinook_api.artist;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when no artist exists with the requested ID.
 *
 * <p>The {@code @ResponseStatus} annotation tells Spring MVC to answer the
 * request with HTTP 404 Not Found when this exception reaches the controller.</p>
 */
@ResponseStatus (HttpStatus.NOT_FOUND)
public class ArtistNotFoundException extends RuntimeException{
    /**
     * Creates the exception with a message that includes the missing ID.
     *
     * @param id the artist ID that was not found
     */
    ArtistNotFoundException(Integer id) {
        super("Artist not found with ID: " + id);
    }
}

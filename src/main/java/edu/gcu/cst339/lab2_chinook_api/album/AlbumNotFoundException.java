package edu.gcu.cst339.lab2_chinook_api.album;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when no album exists with the requested ID.
 *
 * <p>The {@code @ResponseStatus} annotation tells Spring MVC to answer the
 * request with HTTP 404 Not Found when this exception reaches the controller.</p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class AlbumNotFoundException extends RuntimeException {
    
    /**
     * Creates the exception with a message that includes the missing ID.
     *
     * @param id the album ID that was not found
     */
    AlbumNotFoundException(Integer id) {
        super("Album not found with ID: " + id);
    }
}

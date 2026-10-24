package edu.gcu.cst339.lab2_chinook_api.genre;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when no genre exists with the requested ID.
 *
 * <p>The {@code @ResponseStatus} annotation tells Spring MVC to answer the
 * request with HTTP 404 Not Found when this exception reaches the controller.</p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class GenreNotFoundException extends RuntimeException {

    /**
     * Creates the exception with a message that includes the missing ID.
     *
     * @param id the genre ID that was not found
     */
    GenreNotFoundException(Integer id) {
        super("Genre not found with id " + id);
    }
}
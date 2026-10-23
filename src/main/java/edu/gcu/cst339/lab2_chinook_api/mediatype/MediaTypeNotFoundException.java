package edu.gcu.cst339.lab2_chinook_api.mediatype;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when no media type exists with the requested ID.
 *
 * <p>The {@code @ResponseStatus} annotation tells Spring MVC to answer the
 * request with HTTP 404 Not Found when this exception reaches the controller.</p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class MediaTypeNotFoundException extends RuntimeException {
    /**
     * Creates the exception with a message that includes the missing ID.
     *
     * @param mediaTypeId the media type ID that was not found
     */
    MediaTypeNotFoundException(Integer mediaTypeId) {
        super("Media Type not found with ID: " + mediaTypeId);
    }
}

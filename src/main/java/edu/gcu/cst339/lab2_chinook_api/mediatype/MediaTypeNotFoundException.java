package edu.gcu.cst339.lab2_chinook_api.mediatype;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class MediaTypeNotFoundException extends RuntimeException {
    MediaTypeNotFoundException(Integer mediaTypeId) {
        super("Media Type not found with ID: " + mediaTypeId);
    }
}

package edu.gcu.cst339.lab2_chinook_api.album;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class AlbumNotFoundException extends RuntimeException {
    
    AlbumNotFoundException(Integer id) {
        super("Album not found with ID: " + id);
    }
}

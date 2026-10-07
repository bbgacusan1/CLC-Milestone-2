package edu.gcu.cst339.lab2_chinook_api.genre;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class GenreNotFoundException extends RuntimeException {

    GenreNotFoundException(Integer id) {
        super("Genre not found with id " + id);
    }
}
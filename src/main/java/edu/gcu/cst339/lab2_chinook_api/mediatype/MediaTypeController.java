package edu.gcu.cst339.lab2_chinook_api.mediatype;

import java.net.URI;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Media Types", description = "The Media Type API")
@RestController
@RequestMapping("/api/media-types")
class MediaTypeController {
    
    private final MediaTypeService mediaTypeService;
    
    MediaTypeController(MediaTypeService mediaTypeService) {
        this.mediaTypeService = mediaTypeService;
    }

    @Operation(summary = "Get all media types", description = "Returns a list of all media types")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of media types")
    @GetMapping
    List<MediaTypeDto> getAll() {
        return mediaTypeService.findAll();
    }

    @Operation(summary = "Get media type by ID", description = "Returns a single media type by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved media type")
    @ApiResponse(responseCode = "404", description = "Media type not found")
    @GetMapping("/{id}")
    ResponseEntity<MediaTypeDto> getById(@PathVariable Integer id) {
        MediaTypeDto mediaTypeDto = mediaTypeService.findById(id);
        return ResponseEntity.ok(mediaTypeDto);
    }

    @Operation(summary = "Create a new media type", description = "Creates a new media type and returns the created media type")
    @ApiResponse(responseCode = "201", description = "Media type created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @PostMapping
    ResponseEntity<MediaTypeDto> create(@Valid @RequestBody MediaTypeDto mediaTypeDto) {
        MediaTypeDto createdMediaType = mediaTypeService.create(mediaTypeDto);
        return ResponseEntity.created(URI.create("/api/media-types/" + createdMediaType.mediaTypeId())).body(createdMediaType);
    }

    @Operation(summary = "Update a media type", description = "Updates an existing media type and returns the updated media type")
    @ApiResponse(responseCode = "200", description = "Media type updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Media type not found")
    @PutMapping("/{id}")
    ResponseEntity<MediaTypeDto> update(@PathVariable Integer id, @Valid @RequestBody MediaTypeDto mediaTypeDto) {
        MediaTypeDto updatedMediaType = mediaTypeService.update(id, mediaTypeDto);
        return ResponseEntity.ok(updatedMediaType);
    }

    @Operation(summary = "Delete a media type", description = "Deletes an existing media type by its ID")
    @ApiResponse(responseCode = "204", description = "Media type deleted successfully")
    @ApiResponse(responseCode = "404", description = "Media type not found")
    @ApiResponse(responseCode = "409", description = "Conflict: Media type is in use and cannot be deleted")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Integer id) {
        mediaTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Media type is in use by one or more tracks and cannot be deleted.");
    }
}

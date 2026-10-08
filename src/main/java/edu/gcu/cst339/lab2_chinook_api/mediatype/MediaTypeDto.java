package edu.gcu.cst339.lab2_chinook_api.mediatype;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MediaTypeDto(
        Integer mediaTypeId,
        @NotBlank
        @Size(max = 120)
        String name
) {
    static MediaTypeDto fromEntity(MediaType mediaType) {
        return new MediaTypeDto(mediaType.getMediaTypeId(), mediaType.getName());
    }
}
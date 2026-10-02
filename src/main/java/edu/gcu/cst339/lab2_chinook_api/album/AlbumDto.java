package edu.gcu.cst339.lab2_chinook_api.album;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AlbumDto(
    Integer albumId,

    @NotBlank
    @Size(max = 160)
    String title,

    @NotNull
    Integer artistId) {
        
    static AlbumDto fromEntity(Album album) {
        return new AlbumDto(
            album.getAlbumId(),
            album.getTitle(),
            album.getArtistId()
        );
    }
}

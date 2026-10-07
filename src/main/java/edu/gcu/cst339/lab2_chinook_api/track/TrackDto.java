package edu.gcu.cst339.lab2_chinook_api.track;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

record TrackDto(
    Integer trackId,
    @NotNull @Size(max = 200) String name,   
    Integer albumId,
    @NotNull Integer mediaTypeId,
    Integer genreId,
    @Size(max = 220) String composer,
    @NotNull Integer milliseconds,
    Integer bytes,
    @NotNull BigDecimal unitPrice
) {
    static TrackDto fromEntity(Track track) {
        return new TrackDto(
            track.getTrackId(),
            track.getName(),
            track.getAlbumId(),
            track.getMediaTypeId(),
            track.getGenreId(),
            track.getComposer(),
            track.getMilliseconds(),
            track.getBytes(),
            track.getUnitPrice()
        );
    }
}

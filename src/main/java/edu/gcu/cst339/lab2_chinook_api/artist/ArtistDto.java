package edu.gcu.cst339.lab2_chinook_api.artist;

record ArtistDto(
    Integer artistId,
    String name
) {
    static ArtistDto fromEntity(Artist artist) {
        return new ArtistDto(
            artist.getArtistId(), 
            artist.getArtistName()
        );
    }
} 
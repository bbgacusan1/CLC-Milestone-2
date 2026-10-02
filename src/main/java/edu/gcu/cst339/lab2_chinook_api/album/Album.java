package edu.gcu.cst339.lab2_chinook_api.album;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "album")
class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer albumId;
    @Column(name = "title", nullable = false, length = 160)
    private String title;
    @Column(name = "artist_id", nullable = false)
    private Integer artistId;

    protected Album() {}
    
    Album(String title, Integer artistId) {
        this.title = title;
        this.artistId = artistId;
    }
    
    Integer getAlbumId() {
        return albumId;
    }

    String getTitle() {
        return title;
    }

    Integer getArtistId() {
        return artistId;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setArtistId(Integer artistId) {
        this.artistId = artistId;
    }
}

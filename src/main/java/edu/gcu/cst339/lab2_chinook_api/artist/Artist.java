package edu.gcu.cst339.lab2_chinook_api.artist;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "artist")
class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_id", nullable = false)
    private Integer artistId;

    @Size(max = 120)
    @Column(name = "name")
    private String name;

    public Artist() { }
    
    public Artist(String name) {
        this.name = name; 
    }

    // Getters
    Integer getArtistId() { return artistId; }
    String getArtistName() { return name; }
    // Setters
    void setArtistId(Integer artistId) { this.artistId = artistId; }
    void setName(String name) { this.name = name; }

}

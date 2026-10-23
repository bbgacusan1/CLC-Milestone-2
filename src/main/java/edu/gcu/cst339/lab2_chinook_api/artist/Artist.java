package edu.gcu.cst339.lab2_chinook_api.artist;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

/**
 * JPA entity mapped to the Chinook {@code artist} table.
 *
 * <p>An artist has a database-generated ID and an optional name of up to
 * 120 characters. Albums refer to an artist through {@code album.artist_id}.</p>
 */
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

    /**
     * No-argument constructor required by JPA.
     */
    public Artist() { }
    
    /**
     * Creates a new, unsaved artist. The ID is assigned by the database when the
     * artist is saved.
     *
     * @param name the artist name (at most 120 characters)
     */
    public Artist(String name) {
        this.name = name; 
    }

    // Getters
    /** @return the database-generated artist ID, or {@code null} if the artist has not been saved */
    Integer getArtistId() { return artistId; }
    /** @return the artist name */
    String getArtistName() { return name; }
    // Setters
    /** @param artistId the artist ID; normally set by the database rather than by application code */
    void setArtistId(Integer artistId) { this.artistId = artistId; }
    /** @param name the new artist name (at most 120 characters) */
    void setName(String name) { this.name = name; }

}

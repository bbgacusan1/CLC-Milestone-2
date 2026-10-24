package edu.gcu.cst339.lab2_chinook_api.album;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity mapped to the Chinook {@code album} table.
 *
 * <p>An album has a database-generated ID, a title and the ID of the artist who
 * recorded it. {@code artistId} is a foreign key to {@code artist.artist_id};
 * it is stored as a plain ID rather than a JPA relationship, so the database
 * constraint (not this class) guarantees that the artist exists.</p>
 */
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

    /**
     * No-argument constructor required by JPA. Application code should use
     * {@link #Album(String, Integer)} instead.
     */
    protected Album() {}
    
    /**
     * Creates a new, unsaved album. The album ID is assigned by the database
     * when the album is saved.
     *
     * @param title    the album title (required, at most 160 characters)
     * @param artistId the ID of an existing artist who recorded the album
     */
    Album(String title, Integer artistId) {
        this.title = title;
        this.artistId = artistId;
    }
    
    /** {@return the database-generated album ID, or {@code null} if the album has not been saved} */
    Integer getAlbumId() {
        return albumId;
    }

    /** {@return the album title} */
    String getTitle() {
        return title;
    }

    /** {@return the ID of the artist who recorded the album} */
    Integer getArtistId() {
        return artistId;
    }

    /**
     * Sets the album title.
     *
     * @param title the new album title (required, at most 160 characters)
     */
    void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the artist ID.
     *
     * @param artistId the ID of an existing artist to assign the album to
     */
    void setArtistId(Integer artistId) {
        this.artistId = artistId;
    }
}

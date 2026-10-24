package edu.gcu.cst339.lab2_chinook_api.genre;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

/**
 * JPA entity mapped to the Chinook {@code genre} table.
 *
 * <p>A genre has a database-generated ID and a name of up to 120 characters,
 * such as "Rock" or "Jazz". Tracks refer to a genre through
 * {@code track.genre_id}.</p>
 */
@Entity
@Table(name = "genre")
class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id", nullable = false)
    private Integer genreId;

    @Size(max = 120)
    @Column(name = "name")
    private String name;

    /**
     * Creates an empty genre. Used by JPA and by the service before the name is set.
     */
    public Genre() {
    }

    /** {@return the database-generated genre ID, or {@code null} if not yet saved} */
    public Integer getGenreId() { return genreId; }

    /** {@return the genre name} */
    public String getName() { return name; }
    /**
     * Sets the genre name.
     *
     * @param name the new genre name (at most 120 characters)
     */
    public void setName(String name) { this.name = name; }
}
package edu.gcu.cst339.lab2_chinook_api.playlist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

/**
 * JPA entity mapped to the Chinook {@code playlist} table.
 *
 * <p>A playlist has a database-generated ID and a name of up to 120 characters.
 * The tracks on each playlist are stored separately in the
 * {@code playlist_track} table, which refers back to the playlist through
 * {@code playlist_track.playlist_id}.</p>
 */
@Entity
@Table(name = "playlist")
class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Integer playlistId;

    @Size(max = 120)
    @Column(name = "name")
    private String name;

    /** @return the database-generated playlist ID, or {@code null} if not yet saved */
    public Integer getPlaylistId() {
        return playlistId;
    }

    /** @param playlistId the playlist ID; normally set by the database rather than by application code */
    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    /** @return the playlist name */
    public String getName() {
        return name;
    }

    /** @param name the new playlist name (at most 120 characters) */
    public void setName(String name) {
        this.name = name;
    }

}


package edu.gcu.cst339.lab2_chinook_api.playlisttrack;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
 

@Entity
@Table(name = "playlist_track")
@IdClass(PlaylistTrack.PlaylistTrackId.class)
class PlaylistTrack {

    // Foreign key -> playlist.playlist_id
    @Id
    @NotNull
    @Column(name = "playlist_id")
    private Integer playlistId;

    // Foreign key -> track.track_id
    @Id
    @NotNull
    @Column(name = "track_id")
    private Integer trackId;

    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    public Integer getTrackId() {
        return trackId;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    // Composite primary key class (this table's ID uses several columns)
    public static class PlaylistTrackId implements Serializable {

        private static final long serialVersionUID = 1L;

        private Integer playlistId;
        private Integer trackId;

        // JPA requires an empty constructor
        public PlaylistTrackId() {
        }

        // Added so the service can build a key from the two ids in the URL
        public PlaylistTrackId(Integer playlistId, Integer trackId) {
            this.playlistId = playlistId;
            this.trackId = trackId;
        }


        // Two keys are equal when both ids match (JPA uses this to identify rows)
        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof PlaylistTrackId that)) {
                return false;
            }
            return Objects.equals(playlistId, that.playlistId) && Objects.equals(trackId, that.trackId);
        }

        //must be consistent with equals()
        @Override
        public int hashCode() {
            return Objects.hash(playlistId, trackId);
        }
    }

}

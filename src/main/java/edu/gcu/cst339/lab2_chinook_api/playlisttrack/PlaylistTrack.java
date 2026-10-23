package edu.gcu.cst339.lab2_chinook_api.playlisttrack;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
 

/**
 * JPA entity mapped to the Chinook {@code playlist_track} join table.
 *
 * <p>Each row means "this track is on this playlist", linking
 * {@code playlist.playlist_id} to {@code track.track_id}. The table has no
 * single ID column: its primary key is the pair of both IDs, described by the
 * nested {@link PlaylistTrackId} class and declared with {@code @IdClass}.
 * Both IDs are supplied by the client rather than generated.</p>
 */
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

    /** @return the ID of the playlist this row belongs to */
    public Integer getPlaylistId() {
        return playlistId;
    }

    /** @param playlistId the ID of an existing playlist (part of the primary key) */
    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    /** @return the ID of the track on the playlist */
    public Integer getTrackId() {
        return trackId;
    }

    /** @param trackId the ID of an existing track (part of the primary key) */
    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    /**
     * Composite primary key for {@link PlaylistTrack}, made of the playlist ID and
     * the track ID.
     *
     * <p>JPA requires a key class like this when a table's primary key spans
     * several columns. Its field names must match the {@code @Id} fields of the
     * entity, and it must be {@code Serializable} and implement {@code equals}
     * and {@code hashCode}.</p>
     */
    public static class PlaylistTrackId implements Serializable {

        private static final long serialVersionUID = 1L;

        private Integer playlistId;
        private Integer trackId;

        /**
         * No-argument constructor required by JPA.
         */
        public PlaylistTrackId() {
        }

        /**
         * Creates a key from the two IDs, so the service can look up a row using
         * the IDs taken from the URL.
         *
         * @param playlistId the playlist part of the key
         * @param trackId    the track part of the key
         */
        public PlaylistTrackId(Integer playlistId, Integer trackId) {
            this.playlistId = playlistId;
            this.trackId = trackId;
        }


        /**
         * Two keys are equal when both the playlist ID and the track ID match.
         * JPA relies on this to identify rows.
         *
         * @param other the object to compare with
         * @return {@code true} if {@code other} is a key with the same two IDs
         */
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

        /**
         * Returns a hash code built from both IDs, consistent with {@link #equals(Object)}.
         *
         * @return the hash code of this key
         */
        @Override
        public int hashCode() {
            return Objects.hash(playlistId, trackId);
        }
    }

}

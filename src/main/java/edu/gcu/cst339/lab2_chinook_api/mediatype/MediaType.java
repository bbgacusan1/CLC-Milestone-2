package edu.gcu.cst339.lab2_chinook_api.mediatype;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity mapped to the Chinook {@code media_type} table.
 *
 * <p>A media type describes the file format of a track, such as
 * "MPEG audio file" or "AAC audio file". Tracks refer to a media type through
 * the required {@code track.media_type_id} foreign key.</p>
 *
 * <p>Note: this class is unrelated to Spring's {@code org.springframework.http.MediaType}.</p>
 */
@Entity
@Table(name = "media_type")
class MediaType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_type_id")
    private Integer mediaTypeId;

    @Column(name = "name", length = 120)
    private String name;

    /**
     * No-argument constructor required by JPA. Application code should use
     * {@link #MediaType(String)} instead.
     */
    protected MediaType() {
    }

    /**
     * Creates a new, unsaved media type. The ID is assigned by the database when
     * the media type is saved.
     *
     * @param name the media type name (at most 120 characters)
     */
    MediaType(String name) {
        this.name = name;
    }

    /** @return the database-generated media type ID, or {@code null} if not yet saved */
    Integer getMediaTypeId() {
        return mediaTypeId;
    }

    /** @return the media type name */
    String getName() {
        return name;
    }

    /** @param name the new media type name (at most 120 characters) */
    void setName(String name) {
        this.name = name;
    }
}

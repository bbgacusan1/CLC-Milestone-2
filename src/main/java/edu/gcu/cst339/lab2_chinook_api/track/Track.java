package edu.gcu.cst339.lab2_chinook_api.track;

import java.math.BigDecimal;         

import jakarta.persistence.Column;
import jakarta.persistence.Entity;       
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull; 
import jakarta.validation.constraints.Size;    

/**
 * JPA entity mapped to the Chinook {@code track} table.
 *
 * <p>A track is a single song or recording. It stores the name, composer,
 * length in milliseconds, file size in bytes and unit price, plus three
 * foreign keys: {@code albumId} (optional, to {@code album.album_id}),
 * {@code mediaTypeId} (required, to {@code media_type.media_type_id}) and
 * {@code genreId} (optional, to {@code genre.genre_id}). Invoice lines and
 * playlist-track rows refer to a track through {@code track_id}.</p>
 */
@Entity                    
@Table(name = "track")     
class Track {              

    //columns in the track table
    @Id                                                   
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    @Column(name = "track_id")                            
    private Integer trackId;

    @NotNull                      
    @Size(max = 200)              
    @Column(name = "name")
    private String name;

    @Column(name = "album_id")
    private Integer albumId;

    @NotNull
    @Column(name = "media_type_id")
    private Integer mediaTypeId;

    @Column(name = "genre_id")
    private Integer genreId;

    @Size(max = 220)            
    @Column(name = "composer")
    private String composer;

    @NotNull                    
    @Column(name = "milliseconds")
    private Integer milliseconds;

    @Column(name = "bytes")      
    private Integer bytes;

    @NotNull                      
    @Column(name = "unit_price")
    private BigDecimal unitPrice; 

    //getters and setters
    /** @return the database-generated track ID, or {@code null} if not yet saved */
    public Integer getTrackId() {
        return trackId;
    }

    /** @param trackId the track ID; normally set by the database rather than by application code */
    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    /** @return the track name */
    public String getName() {
        return name;
    }

    /** @param name the track name (required, at most 200 characters) */
    public void setName(String name) {
        this.name = name;
    }

    /** @return the ID of the album the track belongs to, or {@code null} if none */
    public Integer getAlbumId() {
        return albumId;
    }

    /** @param albumId the ID of an existing album, or {@code null} */
    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    /** @return the ID of the track's media type (file format) */
    public Integer getMediaTypeId() {
        return mediaTypeId;
    }

    /** @param mediaTypeId the ID of an existing media type (required) */
    public void setMediaTypeId(Integer mediaTypeId) {
        this.mediaTypeId = mediaTypeId;
    }

    /** @return the ID of the track's genre, or {@code null} if none */
    public Integer getGenreId() {
        return genreId;
    }

    /** @param genreId the ID of an existing genre, or {@code null} */
    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    /** @return the composer(s) of the track, or {@code null} if unknown */
    public String getComposer() {
        return composer;
    }

    /** @param composer the composer(s) (optional, at most 220 characters) */
    public void setComposer(String composer) {
        this.composer = composer;
    }

    /** @return the length of the track in milliseconds */
    public Integer getMilliseconds() {
        return milliseconds;
    }

    /** @param milliseconds the length of the track in milliseconds (required) */
    public void setMilliseconds(Integer milliseconds) {
        this.milliseconds = milliseconds;
    }

    /** @return the size of the track's file in bytes, or {@code null} if unknown */
    public Integer getBytes() {
        return bytes;
    }

    /** @param bytes the file size in bytes (optional) */
    public void setBytes(Integer bytes) {
        this.bytes = bytes;
    }

    /** @return the price of one purchase of the track */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /** @param unitPrice the price of one purchase of the track (required) */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
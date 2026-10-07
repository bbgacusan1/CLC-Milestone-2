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
    public Integer getTrackId() {
        return trackId;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Integer getMediaTypeId() {
        return mediaTypeId;
    }

    public void setMediaTypeId(Integer mediaTypeId) {
        this.mediaTypeId = mediaTypeId;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public Integer getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(Integer milliseconds) {
        this.milliseconds = milliseconds;
    }

    public Integer getBytes() {
        return bytes;
    }

    public void setBytes(Integer bytes) {
        this.bytes = bytes;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
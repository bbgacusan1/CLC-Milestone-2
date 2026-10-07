package edu.gcu.cst339.lab2_chinook_api.mediatype;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "media_type")
class MediaType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_type_id")
    private Integer mediaTypeId;

    @Column(name = "name", length = 120)
    private String name;

    protected MediaType() {
    }

    MediaType(String name) {
        this.name = name;
    }

    Integer getMediaTypeId() {
        return mediaTypeId;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }
}

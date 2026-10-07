package edu.gcu.cst339.lab2_chinook_api.genre;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

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

    public Genre() {
    }

    public Integer getGenreId() { return genreId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
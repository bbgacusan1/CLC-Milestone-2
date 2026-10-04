package edu.gcu.cst339.lab2_chinook_api.artist;

import org.springframework.data.jpa.repository.JpaRepository;

interface ArtistRepository extends JpaRepository<Artist, Integer> { }

package edu.gcu.cst339.lab2_chinook_api.album;

import org.springframework.data.jpa.repository.JpaRepository;

interface AlbumRepository extends JpaRepository<Album, Integer> {
    
}

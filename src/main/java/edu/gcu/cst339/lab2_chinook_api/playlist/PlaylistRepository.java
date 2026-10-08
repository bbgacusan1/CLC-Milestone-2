package edu.gcu.cst339.lab2_chinook_api.playlist;

import org.springframework.data.jpa.repository.JpaRepository;

interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
        
}

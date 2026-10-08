package edu.gcu.cst339.lab2_chinook_api.track;
import org.springframework.data.jpa.repository.JpaRepository;

interface TrackRepository extends JpaRepository<Track, Integer> {
        
}

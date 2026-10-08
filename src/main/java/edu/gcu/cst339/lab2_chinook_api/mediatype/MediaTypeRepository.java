package edu.gcu.cst339.lab2_chinook_api.mediatype;

import org.springframework.data.jpa.repository.JpaRepository;

interface MediaTypeRepository extends JpaRepository<MediaType, Integer> {
    
}

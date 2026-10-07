package edu.gcu.cst339.lab2_chinook_api.genre;

import org.springframework.data.jpa.repository.JpaRepository;

interface GenreRepository extends JpaRepository<Genre, Integer> {
}
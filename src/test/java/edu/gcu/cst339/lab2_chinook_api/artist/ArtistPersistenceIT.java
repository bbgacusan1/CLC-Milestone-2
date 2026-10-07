package edu.gcu.cst339.lab2_chinook_api.artist;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
class ArtistPersistenceIT {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savedArtist_isWrittenToPostgres() {
        Artist saved = artistRepository.save(new Artist("Persistence Test Artist"));
        entityManager.flush(); // forces the INSERT to run against PostgreSQL

        assertThat(saved.getArtistId()).isNotNull();

        // Raw SQL bypasses Hibernate's cache and reads the actual table row
        Object name = entityManager
                .createNativeQuery("select name from artist where artist_id = ?1")
                .setParameter(1, saved.getArtistId())
                .getSingleResult();

        assertThat(name).isEqualTo("Persistence Test Artist");
    }

    @Test
    void connectedDatabase_isPostgres() {
        Object version = entityManager.createNativeQuery("select version()").getSingleResult();

        assertThat(version.toString()).startsWith("PostgreSQL");
    }
}
package edu.gcu.cst339.lab2_chinook_api.album;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AlbumRepositoryTests {
    @Autowired
    private AlbumRepository albumRepository;

    @Test
    void countReturnsAllChinookAlbums() {
        assertThat(albumRepository.count()).isGreaterThanOrEqualTo(347);    
    }

    @Test
    void findByIdReturnsAlbum() {
        Album album = albumRepository.findById(1).orElseThrow();

        assertThat(album.getTitle()).isEqualTo("For Those About To Rock We Salute You");

        assertThat(album.getArtistId()).isEqualTo(1);
    }
}

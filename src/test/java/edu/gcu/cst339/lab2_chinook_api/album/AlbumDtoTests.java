package edu.gcu.cst339.lab2_chinook_api.album;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class AlbumDtoTests {
    @Test
    void fromEntityCopiesFields() {
        Album album = new Album("Test Album", 5);

        AlbumDto albumDto = AlbumDto.fromEntity(album);
        assertThat(albumDto.title()).isEqualTo("Test Album");
        assertThat(albumDto.artistId()).isEqualTo(5);
    }
}

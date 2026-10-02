package edu.gcu.cst339.lab2_chinook_api.album;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTests {

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private AlbumService albumService;

    @Test
    void findByIdReturnsDtoWhenFound() {
        when(albumRepository.findById(1)).thenReturn(Optional.of(new Album("Test Album", 5)));

        AlbumDto result = albumService.findById(1);

        assertThat(result.title()).isEqualTo("Test Album");
        assertThat(result.artistId()).isEqualTo(5);
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(albumRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> albumService.findById(999))
                .isInstanceOf(AlbumNotFoundException.class);
    }

    @Test
    void createSavesNewAlbum() {
        when(albumRepository.save(any(Album.class))).thenAnswer(call -> call.getArgument(0));

        AlbumDto result = albumService.create(new AlbumDto(null, "New Album", 3));

        assertThat(result.title()).isEqualTo("New Album");
        verify(albumRepository).save(any(Album.class));
    }

    @Test
    void deleteThrowsWhenMissing() {
        when(albumRepository.existsById(999)).thenReturn(false);

        assertThatThrownBy(() -> albumService.delete(999))
                .isInstanceOf(AlbumNotFoundException.class);
        verify(albumRepository, never()).deleteById(any());
    }
}
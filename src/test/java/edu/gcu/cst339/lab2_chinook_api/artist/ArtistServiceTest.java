package edu.gcu.cst339.lab2_chinook_api.artist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistService artistService;

    private Artist artist(int id, String name) {
        Artist artist = new Artist(name);
        ReflectionTestUtils.setField(artist, "artistId", id);
        return artist;
    }

    @Test
    void findAll_returnsAllArtistsAsDtos() {
        when(artistRepository.findAll())
                .thenReturn(List.of(artist(1, "AC/DC"), artist(2, "Accept")));

        List<ArtistDto> result = artistService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("AC/DC");
        assertThat(result.get(1).artistId()).isEqualTo(2);
    }

    @Test
    void findById_existingId_returnsDto() {
        when(artistRepository.findById(1)).thenReturn(Optional.of(artist(1, "AC/DC")));

        ArtistDto result = artistService.findById(1);

        assertThat(result.artistId()).isEqualTo(1);
        assertThat(result.name()).isEqualTo("AC/DC");
    }

    @Test
    void findById_missingId_throwsNotFound() {
        when(artistRepository.findById(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> artistService.findById(99999))
                .isInstanceOf(ArtistNotFoundException.class);
    }

    @Test
    void create_savesArtistAndReturnsDtoWithGeneratedId() {
        when(artistRepository.save(any(Artist.class))).thenAnswer(invocation -> {
            Artist toSave = invocation.getArgument(0);
            ReflectionTestUtils.setField(toSave, "artistId", 276);
            return toSave;
        });

        ArtistDto result = artistService.create(new ArtistDto(null, "New Artist"));

        assertThat(result.artistId()).isEqualTo(276);
        assertThat(result.name()).isEqualTo("New Artist");
        verify(artistRepository).save(any(Artist.class));
    }

    @Test
    void update_existingId_changesName() {
        Artist existing = artist(5, "Old Name");
        when(artistRepository.findById(5)).thenReturn(Optional.of(existing));

        ArtistDto result = artistService.update(5, new ArtistDto(null, "New Name"));

        assertThat(result.artistId()).isEqualTo(5);
        assertThat(result.name()).isEqualTo("New Name");
        assertThat(existing.getArtistName()).isEqualTo("New Name");
    }

    @Test
    void update_missingId_throwsNotFound() {
        when(artistRepository.findById(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> artistService.update(99999, new ArtistDto(null, "X")))
                .isInstanceOf(ArtistNotFoundException.class);
    }

    @Test
    void delete_existingId_deletesArtist() {
        when(artistRepository.existsById(5)).thenReturn(true);

        artistService.delete(5);

        verify(artistRepository).deleteById(5);
    }

    @Test
    void delete_missingId_throwsNotFoundAndDeletesNothing() {
        when(artistRepository.existsById(99999)).thenReturn(false);

        assertThatThrownBy(() -> artistService.delete(99999))
                .isInstanceOf(ArtistNotFoundException.class);
        verify(artistRepository, never()).deleteById(any());
    }
}
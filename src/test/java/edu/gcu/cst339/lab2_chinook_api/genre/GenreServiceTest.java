package edu.gcu.cst339.lab2_chinook_api.genre;

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
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    private Genre genre(int id, String name) {
        Genre g = new Genre();
        ReflectionTestUtils.setField(g, "genreId", id);
        g.setName(name);
        return g;
    }

    @Test
    void findAll_returnsAllGenresAsDtos() {
        when(genreRepository.findAll())
                .thenReturn(List.of(genre(1, "Rock"), genre(2, "Jazz")));

        List<GenreDto> result = genreService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Rock");
        assertThat(result.get(1).genreId()).isEqualTo(2);
    }

    @Test
    void findById_existingId_returnsDto() {
        when(genreRepository.findById(1)).thenReturn(Optional.of(genre(1, "Rock")));

        GenreDto result = genreService.findById(1);

        assertThat(result.genreId()).isEqualTo(1);
        assertThat(result.name()).isEqualTo("Rock");
    }

    @Test
    void findById_missingId_throwsNotFound() {
        when(genreRepository.findById(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> genreService.findById(99999))
                .isInstanceOf(GenreNotFoundException.class);
    }

    @Test
    void create_savesGenreAndReturnsDtoWithGeneratedId() {
        when(genreRepository.save(any(Genre.class))).thenAnswer(invocation -> {
            Genre toSave = invocation.getArgument(0);
            ReflectionTestUtils.setField(toSave, "genreId", 26);
            return toSave;
        });

        GenreDto result = genreService.create(new GenreDto(null, "Test Genre"));

        assertThat(result.genreId()).isEqualTo(26);
        assertThat(result.name()).isEqualTo("Test Genre");
        verify(genreRepository).save(any(Genre.class));
    }

    @Test
    void update_existingId_changesNameButNotId() {
        Genre existing = genre(5, "Old Name");
        when(genreRepository.findById(5)).thenReturn(Optional.of(existing));

        GenreDto result = genreService.update(5, new GenreDto(null, "New Name"));

        assertThat(result.genreId()).isEqualTo(5);
        assertThat(result.name()).isEqualTo("New Name");
        assertThat(existing.getName()).isEqualTo("New Name");
    }

    @Test
    void update_missingId_throwsNotFound() {
        when(genreRepository.findById(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> genreService.update(99999, new GenreDto(null, "X")))
                .isInstanceOf(GenreNotFoundException.class);
    }

    @Test
    void delete_existingId_deletesGenre() {
        when(genreRepository.existsById(5)).thenReturn(true);

        genreService.delete(5);

        verify(genreRepository).deleteById(5);
    }

    @Test
    void delete_missingId_throwsNotFoundAndDeletesNothing() {
        when(genreRepository.existsById(99999)).thenReturn(false);

        assertThatThrownBy(() -> genreService.delete(99999))
                .isInstanceOf(GenreNotFoundException.class);
        verify(genreRepository, never()).deleteById(any());
    }
}
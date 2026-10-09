package edu.gcu.cst339.lab2_chinook_api.mediatype;

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
class MediaTypeServiceTest {

    @Mock
    private MediaTypeRepository mediaTypeRepository;

    @InjectMocks
    private MediaTypeService mediaTypeService;

    private MediaType mediaType(int id, String name) {
        MediaType mediaType = new MediaType(name);
        ReflectionTestUtils.setField(mediaType, "mediaTypeId", id);
        return mediaType;
    }

    @Test
    void findAll_returnsAllMediaTypesAsDtos() {
        when(mediaTypeRepository.findAll())
                .thenReturn(List.of(mediaType(1, "MPEG audio file"), mediaType(2, "AAC audio file")));

        List<MediaTypeDto> result = mediaTypeService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("MPEG audio file");
        assertThat(result.get(1).mediaTypeId()).isEqualTo(2);
    }

    @Test
    void findById_existingId_returnsDto() {
        when(mediaTypeRepository.findById(1)).thenReturn(Optional.of(mediaType(1, "MPEG audio file")));

        MediaTypeDto result = mediaTypeService.findById(1);

        assertThat(result.mediaTypeId()).isEqualTo(1);
        assertThat(result.name()).isEqualTo("MPEG audio file");
    }

    @Test
    void findById_missingId_throwsNotFound() {
        when(mediaTypeRepository.findById(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mediaTypeService.findById(99999))
                .isInstanceOf(MediaTypeNotFoundException.class)
                .hasMessageContaining("99999");
    }

    @Test
    void create_savesMediaTypeAndReturnsDtoWithGeneratedId() {
        when(mediaTypeRepository.save(any(MediaType.class))).thenAnswer(invocation -> {
            MediaType toSave = invocation.getArgument(0);
            ReflectionTestUtils.setField(toSave, "mediaTypeId", 6);
            return toSave;
        });

        MediaTypeDto result = mediaTypeService.create(new MediaTypeDto(null, "Test Format"));

        assertThat(result.mediaTypeId()).isEqualTo(6);
        assertThat(result.name()).isEqualTo("Test Format");
        verify(mediaTypeRepository).save(any(MediaType.class));
    }

    @Test
    void update_existingId_changesNameButNotId() {
        MediaType existing = mediaType(5, "Old Name");
        when(mediaTypeRepository.findById(5)).thenReturn(Optional.of(existing));
        when(mediaTypeRepository.save(existing)).thenReturn(existing);

        MediaTypeDto result = mediaTypeService.update(5, new MediaTypeDto(999, "New Name"));

        assertThat(result.mediaTypeId()).isEqualTo(5);
        assertThat(result.name()).isEqualTo("New Name");
        assertThat(existing.getName()).isEqualTo("New Name");
    }

    @Test
    void update_missingId_throwsNotFound() {
        when(mediaTypeRepository.findById(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mediaTypeService.update(99999, new MediaTypeDto(null, "X")))
                .isInstanceOf(MediaTypeNotFoundException.class);
        verify(mediaTypeRepository, never()).save(any());
    }

    @Test
    void delete_existingId_deletesMediaType() {
        when(mediaTypeRepository.existsById(5)).thenReturn(true);

        mediaTypeService.delete(5);

        verify(mediaTypeRepository).deleteById(5);
    }

    @Test
    void delete_missingId_throwsNotFoundAndDeletesNothing() {
        when(mediaTypeRepository.existsById(99999)).thenReturn(false);

        assertThatThrownBy(() -> mediaTypeService.delete(99999))
                .isInstanceOf(MediaTypeNotFoundException.class);
        verify(mediaTypeRepository, never()).deleteById(any());
    }
}

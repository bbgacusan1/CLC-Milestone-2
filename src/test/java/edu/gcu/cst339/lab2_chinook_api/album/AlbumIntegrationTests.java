package edu.gcu.cst339.lab2_chinook_api.album;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AlbumIntegrationTests {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlbumRepository albumRepository;

    @Test
    void getByIdReturnsAlbum() throws Exception {
        mockMvc.perform(get("/api/albums/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.albumId").value(1))
                .andExpect(jsonPath("$.title").value("For Those About To Rock We Salute You"))
                .andExpect(jsonPath("$.artistId").value(1));
    }

    @Test
    void getAllReturnsAlbums() throws Exception {
        mockMvc.perform(get("/api/albums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(347)));
    }

    @Test
    void postCreatesAlbum() throws Exception {
        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Integration Test Album", "artistId": 1}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.albumId").isNumber())
                .andExpect(jsonPath("$.title").value("Integration Test Album"));
    }

    @Test
    void putUpdatesAlbum() throws Exception {
        Album album = albumRepository.save(new Album("Temp Album", 1));
        Integer id = album.getAlbumId();

        mockMvc.perform(put("/api/albums/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Updated Album Title", "artistId": 1}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.albumId").value(id))
                .andExpect(jsonPath("$.title").value("Updated Album Title"));
    }

    @Test
    void deleteRemovesAlbum() throws Exception {
        Album album = albumRepository.save(new Album("Temp Album", 1));
        Integer id = album.getAlbumId();

        mockMvc.perform(delete("/api/albums/" + id))
                .andExpect(status().isNoContent());
        assertThat(albumRepository.existsById(id)).isFalse();
    }
}


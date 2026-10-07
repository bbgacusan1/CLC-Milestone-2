package edu.gcu.cst339.lab2_chinook_api.artist;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@Transactional
class ArtistControllerIT {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void findAll_returnsExistingChinookArtists() throws Exception {
        mockMvc.perform(get("/api/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @Test
    void crudLifecycle_createReadUpdateDelete() throws Exception {
        // CREATE
        String body = mockMvc.perform(post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"IT Test Artist\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("IT Test Artist"))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(body, "$.artistId");

        // READ
        mockMvc.perform(get("/api/artists/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artistId").value(id))
                .andExpect(jsonPath("$.name").value("IT Test Artist"));

        // UPDATE
        mockMvc.perform(put("/api/artists/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"IT Test Artist (Renamed)\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("IT Test Artist (Renamed)"));

        // DELETE
        mockMvc.perform(delete("/api/artists/" + id))
                .andExpect(status().isNoContent());

        // CONFIRM GONE
        mockMvc.perform(get("/api/artists/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_missingId_returns404() throws Exception {
        mockMvc.perform(get("/api/artists/99999"))
                .andExpect(status().isNotFound());
    }
}
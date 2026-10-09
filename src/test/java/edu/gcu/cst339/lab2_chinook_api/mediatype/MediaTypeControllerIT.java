package edu.gcu.cst339.lab2_chinook_api.mediatype;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@Transactional
class MediaTypeControllerIT {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void findAll_returnsExistingChinookMediaTypes() throws Exception {
        mockMvc.perform(get("/api/media-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(5)));
    }

    @Test
    void findById_existingId_returnsMediaType() throws Exception {
        mockMvc.perform(get("/api/media-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mediaTypeId").value(1))
                .andExpect(jsonPath("$.name").value("MPEG audio file"));
    }

    @Test
    void crudLifecycle_createReadUpdateDelete() throws Exception {
        // CREATE
        String body = mockMvc.perform(post("/api/media-types")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"IT Test Format\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("IT Test Format"))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(body, "$.mediaTypeId");

        // READ
        mockMvc.perform(get("/api/media-types/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("IT Test Format"));

        // UPDATE
        mockMvc.perform(put("/api/media-types/" + id)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"IT Test Format (Renamed)\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mediaTypeId").value(id))
                .andExpect(jsonPath("$.name").value("IT Test Format (Renamed)"));

        // DELETE
        mockMvc.perform(delete("/api/media-types/" + id))
                .andExpect(status().isNoContent());

        // CONFIRM GONE
        mockMvc.perform(get("/api/media-types/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_blankName_returns400() throws Exception {
        mockMvc.perform(post("/api/media-types")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_nameTooLong_returns400() throws Exception {
        String longName = "x".repeat(121);
        mockMvc.perform(put("/api/media-types/1")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + longName + "\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_missingId_returns404() throws Exception {
        mockMvc.perform(get("/api/media-types/99999"))
                .andExpect(status().isNotFound());
    }

    // Runs without the test transaction so the DELETE is actually sent to PostgreSQL.
    // Inside a test transaction Hibernate would delay the DELETE until commit (which never
    // happens), so the foreign key would never be checked. The database rejects this delete,
    // so no data is changed.
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void delete_mediaTypeUsedByTracks_returns409() throws Exception {
        mockMvc.perform(delete("/api/media-types/1"))
                .andExpect(status().isConflict())
                .andExpect(content().string(
                        "Conflict: Media type is in use by one or more tracks and cannot be deleted."));
    }
}

package edu.gcu.cst339.lab2_chinook_api.customer;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
class CustomerControllerIT {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void findAll_returnsExistingChinookCustomers() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @Test
    void crudLifecycle_createReadUpdateDelete() throws Exception {
        // CREATE
        String body = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName": "IT", "lastName": "Customer",
                                 "email": "it.customer@example.com", "city": "Phoenix"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.firstName").value("IT"))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(body, "$.customerId");

        // READ
        mockMvc.perform(get("/api/customers/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("it.customer@example.com"));

        // UPDATE
        mockMvc.perform(put("/api/customers/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName": "IT", "lastName": "Customer",
                                 "email": "it.customer@example.com", "city": "Glendale"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Glendale"));

        // DELETE
        mockMvc.perform(delete("/api/customers/" + id))
                .andExpect(status().isNoContent());

        // CONFIRM GONE
        mockMvc.perform(get("/api/customers/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_missingRequiredFields_returns400() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_invalidEmail_returns400() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName": "IT", "lastName": "Customer", "email": "not-an-email"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_missingId_returns404() throws Exception {
        mockMvc.perform(get("/api/customers/99999"))
                .andExpect(status().isNotFound());
    }
}
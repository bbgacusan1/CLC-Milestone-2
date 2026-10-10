package edu.gcu.cst339.lab2_chinook_api.invoiceline;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@Transactional
class InvoiceLineControllerIT {

    private static final String CONFLICT_MESSAGE =
            "Request conflicts with existing data: the invoiceId or trackId may not exist.";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    // Builds a request body. unitPrice is passed as raw JSON so tests can send bad values.
    private String invoiceLineJson(int invoiceId, int trackId, String unitPrice, int quantity) {
        return """
                {
                  "invoiceId": %d,
                  "trackId": %d,
                  "unitPrice": %s,
                  "quantity": %d
                }
                """.formatted(invoiceId, trackId, unitPrice, quantity);
    }

    @Test
    void findAll_returnsExistingChinookInvoiceLines() throws Exception {
        mockMvc.perform(get("/api/invoice-lines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(2240)));
    }

    @Test
    void findById_existingId_returnsInvoiceLine() throws Exception {
        mockMvc.perform(get("/api/invoice-lines/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceLineId").value(1))
                .andExpect(jsonPath("$.invoiceId").value(1))
                .andExpect(jsonPath("$.trackId").value(2))
                .andExpect(jsonPath("$.unitPrice").value(0.99))
                .andExpect(jsonPath("$.quantity").value(1));
    }

    @Test
    void findById_missingId_returns404() throws Exception {
        mockMvc.perform(get("/api/invoice-lines/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crudLifecycle_createReadUpdateDelete() throws Exception {
        // CREATE
        String body = mockMvc.perform(post("/api/invoice-lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceLineJson(1, 3, "0.99", 1)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.invoiceId").value(1))
                .andExpect(jsonPath("$.trackId").value(3))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(body, "$.invoiceLineId");

        // READ
        mockMvc.perform(get("/api/invoice-lines/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unitPrice").value(0.99))
                .andExpect(jsonPath("$.quantity").value(1));

        // UPDATE
        mockMvc.perform(put("/api/invoice-lines/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceLineJson(2, 10, "1.99", 3)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceLineId").value(id))
                .andExpect(jsonPath("$.invoiceId").value(2))
                .andExpect(jsonPath("$.trackId").value(10))
                .andExpect(jsonPath("$.unitPrice").value(1.99))
                .andExpect(jsonPath("$.quantity").value(3));

        // DELETE
        mockMvc.perform(delete("/api/invoice-lines/" + id))
                .andExpect(status().isNoContent());

        // CONFIRM GONE
        mockMvc.perform(get("/api/invoice-lines/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_missingRequiredFields_returns400() throws Exception {
        mockMvc.perform(post("/api/invoice-lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": 1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_zeroQuantity_returns400() throws Exception {
        mockMvc.perform(post("/api/invoice-lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceLineJson(1, 3, "0.99", 0)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_negativeUnitPrice_returns400() throws Exception {
        mockMvc.perform(post("/api/invoice-lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceLineJson(1, 3, "-0.99", 1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_unitPriceWithThreeDecimals_returns400() throws Exception {
        mockMvc.perform(post("/api/invoice-lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceLineJson(1, 3, "0.999", 1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_negativeQuantity_returns400() throws Exception {
        mockMvc.perform(put("/api/invoice-lines/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceLineJson(1, 2, "0.99", -1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_missingId_returns404() throws Exception {
        mockMvc.perform(put("/api/invoice-lines/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceLineJson(1, 3, "0.99", 1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_missingId_returns404() throws Exception {
        mockMvc.perform(delete("/api/invoice-lines/99999"))
                .andExpect(status().isNotFound());
    }

    // INSERT runs immediately with IDENTITY ids, so the foreign keys are checked inside the test transaction.
    @Test
    void create_invoiceDoesNotExist_returns409() throws Exception {
        mockMvc.perform(post("/api/invoice-lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceLineJson(99999, 3, "0.99", 1)))
                .andExpect(status().isConflict())
                .andExpect(content().string(CONFLICT_MESSAGE));
    }

    @Test
    void create_trackDoesNotExist_returns409() throws Exception {
        mockMvc.perform(post("/api/invoice-lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceLineJson(1, 99999, "0.99", 1)))
                .andExpect(status().isConflict())
                .andExpect(content().string(CONFLICT_MESSAGE));
    }

    // Runs without the test transaction so the UPDATE is actually sent to PostgreSQL.
    // Inside a test transaction Hibernate would delay the UPDATE until commit (which never
    // happens), so the foreign key would never be checked. The database rejects this update,
    // so no data is changed.
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void update_trackDoesNotExist_returns409() throws Exception {
        mockMvc.perform(put("/api/invoice-lines/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceLineJson(1, 99999, "0.99", 1)))
                .andExpect(status().isConflict())
                .andExpect(content().string(CONFLICT_MESSAGE));
    }
}

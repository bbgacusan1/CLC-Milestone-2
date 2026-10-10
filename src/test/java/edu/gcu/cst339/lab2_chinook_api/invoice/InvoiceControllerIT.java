package edu.gcu.cst339.lab2_chinook_api.invoice;

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
class InvoiceControllerIT {

    private static final String CONFLICT_MESSAGE =
            "Request conflicts with existing data: the customerId may not exist, or the invoice still has invoice lines.";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    // Builds a request body. total and invoiceDate are passed as raw JSON so tests can send bad values.
    private String invoiceJson(int customerId, String invoiceDate, String total) {
        return """
                {
                  "customerId": %d,
                  "invoiceDate": %s,
                  "billingAddress": "123 Test St",
                  "billingCity": "Phoenix",
                  "billingState": "AZ",
                  "billingCountry": "USA",
                  "billingPostalCode": "85001",
                  "total": %s
                }
                """.formatted(customerId, invoiceDate, total);
    }

    @Test
    void findAll_returnsExistingChinookInvoices() throws Exception {
        mockMvc.perform(get("/api/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(412)));
    }

    @Test
    void findById_existingId_returnsInvoice() throws Exception {
        mockMvc.perform(get("/api/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceId").value(1))
                .andExpect(jsonPath("$.customerId").value(2))
                .andExpect(jsonPath("$.invoiceDate").value("2021-01-01T00:00:00"))
                .andExpect(jsonPath("$.billingCity").value("Stuttgart"))
                .andExpect(jsonPath("$.billingCountry").value("Germany"))
                .andExpect(jsonPath("$.total").value(1.98));
    }

    @Test
    void findById_missingId_returns404() throws Exception {
        mockMvc.perform(get("/api/invoices/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crudLifecycle_createReadUpdateDelete() throws Exception {
        // CREATE
        String body = mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceJson(1, "\"2026-10-09T12:30:00\"", "9.99")))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.total").value(9.99))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(body, "$.invoiceId");

        // READ
        mockMvc.perform(get("/api/invoices/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceDate").value("2026-10-09T12:30:00"))
                .andExpect(jsonPath("$.billingCity").value("Phoenix"));

        // UPDATE
        mockMvc.perform(put("/api/invoices/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceJson(3, "\"2026-10-10T08:00:00\"", "12.50")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceId").value(id))
                .andExpect(jsonPath("$.customerId").value(3))
                .andExpect(jsonPath("$.invoiceDate").value("2026-10-10T08:00:00"))
                .andExpect(jsonPath("$.total").value(12.5));

        // DELETE
        mockMvc.perform(delete("/api/invoices/" + id))
                .andExpect(status().isNoContent());

        // CONFIRM GONE
        mockMvc.perform(get("/api/invoices/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_missingRequiredFields_returns400() throws Exception {
        mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"billingCity\": \"Phoenix\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_negativeTotal_returns400() throws Exception {
        mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceJson(1, "\"2026-10-09T12:30:00\"", "-1.00")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_totalWithThreeDecimals_returns400() throws Exception {
        mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceJson(1, "\"2026-10-09T12:30:00\"", "1.999")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_badDateFormat_returns400() throws Exception {
        mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceJson(1, "\"10/09/2026\"", "9.99")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_postalCodeTooLong_returns400() throws Exception {
        String json = invoiceJson(2, "\"2021-01-01T00:00:00\"", "1.98")
                .replace("\"85001\"", "\"12345678901\"");
        mockMvc.perform(put("/api/invoices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_missingId_returns404() throws Exception {
        mockMvc.perform(put("/api/invoices/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceJson(1, "\"2026-10-09T12:30:00\"", "9.99")))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_missingId_returns404() throws Exception {
        mockMvc.perform(delete("/api/invoices/99999"))
                .andExpect(status().isNotFound());
    }

    // INSERT runs immediately with IDENTITY ids, so the foreign key is checked inside the test transaction.
    @Test
    void create_customerDoesNotExist_returns409() throws Exception {
        mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceJson(9999, "\"2026-10-09T12:30:00\"", "9.99")))
                .andExpect(status().isConflict())
                .andExpect(content().string(CONFLICT_MESSAGE));
    }

    // The next two tests run without the test transaction so the UPDATE/DELETE is actually sent to
    // PostgreSQL. Inside a test transaction Hibernate would delay it until commit (which never
    // happens), so the foreign key would never be checked. The database rejects both, so no data
    // is changed.
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void update_customerDoesNotExist_returns409() throws Exception {
        mockMvc.perform(put("/api/invoices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceJson(9999, "\"2021-01-01T00:00:00\"", "1.98")))
                .andExpect(status().isConflict())
                .andExpect(content().string(CONFLICT_MESSAGE));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void delete_invoiceWithInvoiceLines_returns409() throws Exception {
        mockMvc.perform(delete("/api/invoices/1"))
                .andExpect(status().isConflict())
                .andExpect(content().string(CONFLICT_MESSAGE));
    }
}

package edu.gcu.cst339.lab2_chinook_api.invoice;

import java.net.URI;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Invoices", description = "The Invoice API")
@RestController
@RequestMapping("/api/invoices")
class InvoiceController {
    
    private final InvoiceService invoiceService;
    
    InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Operation(summary = "Get all invoices", description = "Returns a list of all invoices")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of invoices")
    @GetMapping
    List<InvoiceDto> getAll() {
        return invoiceService.findAll();
    }

    @Operation(summary = "Get invoice by ID", description = "Returns a single invoice by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved invoice")
    @ApiResponse(responseCode = "404", description = "Invoice not found")
    @GetMapping("/{id}")
    ResponseEntity<InvoiceDto> getById(@PathVariable Integer id) {
        InvoiceDto invoiceDto = invoiceService.findById(id);
        return ResponseEntity.ok(invoiceDto);
    }

    @Operation(summary = "Create a new invoice", description = "Creates a new invoice and returns the created invoice")
    @ApiResponse(responseCode = "201", description = "Invoice created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "409", description = "Conflict: customerId does not exist")
    @PostMapping
    ResponseEntity<InvoiceDto> create(@Valid @RequestBody InvoiceDto invoiceDto) {
        InvoiceDto createdInvoice = invoiceService.create(invoiceDto);
        return ResponseEntity.created(URI.create("/api/invoices/" + createdInvoice.invoiceId())).body(createdInvoice);
    }

    @Operation(summary = "Update an invoice", description = "Updates an existing invoice and returns the updated invoice")
    @ApiResponse(responseCode = "200", description = "Invoice updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Invoice not found")
    @ApiResponse(responseCode = "409", description = "Conflict: customerId does not exist")
    @PutMapping("/{id}")
    ResponseEntity<InvoiceDto> update(@PathVariable Integer id, @Valid @RequestBody InvoiceDto invoiceDto) {
        InvoiceDto updatedInvoice = invoiceService.update(id, invoiceDto);
        return ResponseEntity.ok(updatedInvoice);
    }

    @Operation(summary = "Delete an invoice", description = "Deletes an existing invoice by its ID")
    @ApiResponse(responseCode = "204", description = "Invoice deleted successfully")
    @ApiResponse(responseCode = "404", description = "Invoice not found")
    @ApiResponse(responseCode = "409", description = "Conflict: Invoice still has invoice lines and cannot be deleted")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Integer id) {
        invoiceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Request conflicts with existing data: the customerId may not exist, or the invoice still has invoice lines.");
    }
}

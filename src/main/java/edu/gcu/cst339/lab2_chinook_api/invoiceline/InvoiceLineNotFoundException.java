package edu.gcu.cst339.lab2_chinook_api.invoiceline;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class InvoiceLineNotFoundException extends RuntimeException {
    InvoiceLineNotFoundException(Integer invoiceLineId) {
        super("Invoice line not found with ID: " + invoiceLineId);
    }
}

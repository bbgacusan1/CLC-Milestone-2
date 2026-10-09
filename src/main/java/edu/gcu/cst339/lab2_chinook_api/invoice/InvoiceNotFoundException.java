package edu.gcu.cst339.lab2_chinook_api.invoice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class InvoiceNotFoundException extends RuntimeException {
    InvoiceNotFoundException(Integer invoiceId) {
        super("Invoice not found with ID: " + invoiceId);
    }
}

package edu.gcu.cst339.lab2_chinook_api.invoice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when no invoice exists with the requested ID.
 *
 * <p>The {@code @ResponseStatus} annotation tells Spring MVC to answer the
 * request with HTTP 404 Not Found when this exception reaches the controller.</p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class InvoiceNotFoundException extends RuntimeException {
    /**
     * Creates the exception with a message that includes the missing ID.
     *
     * @param invoiceId the invoice ID that was not found
     */
    InvoiceNotFoundException(Integer invoiceId) {
        super("Invoice not found with ID: " + invoiceId);
    }
}

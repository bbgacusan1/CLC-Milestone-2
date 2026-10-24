package edu.gcu.cst339.lab2_chinook_api.invoiceline;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when no invoice line exists with the requested ID.
 *
 * <p>The {@code @ResponseStatus} annotation tells Spring MVC to answer the
 * request with HTTP 404 Not Found when this exception reaches the controller.</p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class InvoiceLineNotFoundException extends RuntimeException {
    /**
     * Creates the exception with a message that includes the missing ID.
     *
     * @param invoiceLineId the invoice line ID that was not found
     */
    InvoiceLineNotFoundException(Integer invoiceLineId) {
        super("Invoice line not found with ID: " + invoiceLineId);
    }
}

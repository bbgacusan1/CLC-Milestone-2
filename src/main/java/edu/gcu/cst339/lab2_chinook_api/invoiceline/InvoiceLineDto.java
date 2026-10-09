package edu.gcu.cst339.lab2_chinook_api.invoiceline;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InvoiceLineDto(
    Integer invoiceLineId,
    @NotNull
    Integer invoiceId,
    @NotNull
    Integer trackId,
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 8, fraction = 2)
    BigDecimal unitPrice,
    @NotNull
    @Positive
    Integer quantity) {
    static InvoiceLineDto fromEntity(InvoiceLine invoiceLine) {
        return new InvoiceLineDto(
            invoiceLine.getInvoiceLineId(),
            invoiceLine.getInvoiceId(),
            invoiceLine.getTrackId(),
            invoiceLine.getUnitPrice(),
            invoiceLine.getQuantity()
        );
    }
}

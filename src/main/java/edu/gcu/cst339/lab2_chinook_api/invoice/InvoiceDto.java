package edu.gcu.cst339.lab2_chinook_api.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InvoiceDto(
        Integer invoiceId,
        @NotNull
        Integer customerId,
        @NotNull
        LocalDateTime invoiceDate,
        @Size(max = 70)
        String billingAddress,
        @Size(max = 40)
        String billingCity,
        @Size(max = 40)
        String billingState,
        @Size(max = 40)
        String billingCountry,
        @Size(max = 10)
        String billingPostalCode,
        @NotNull
        @DecimalMin("0.00")
        @Digits(integer = 8, fraction = 2)
        BigDecimal total
) {
    static InvoiceDto fromEntity(Invoice invoice) {
        return new InvoiceDto(
                invoice.getInvoiceId(),
                invoice.getCustomerId(),
                invoice.getInvoiceDate(),
                invoice.getBillingAddress(),
                invoice.getBillingCity(),
                invoice.getBillingState(),
                invoice.getBillingCountry(),
                invoice.getBillingPostalCode(),
                invoice.getTotal()
        );
    }
}
package edu.gcu.cst339.lab2_chinook_api.invoiceline;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoice_line")
class InvoiceLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_line_id", nullable = false)
    private Integer invoiceLineId;
    
    @Column(name = "invoice_id", nullable = false)
    private Integer invoiceId;

    @Column(name = "track_id", nullable = false)
    private Integer trackId;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    protected InvoiceLine() {
    }

    InvoiceLine(Integer invoiceId, Integer trackId, BigDecimal unitPrice, Integer quantity) {
        this.invoiceId = invoiceId;
        this.trackId = trackId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    Integer getInvoiceLineId() {
        return invoiceLineId;
    }

    Integer getInvoiceId() {
        return invoiceId;
    }

    void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    Integer getTrackId() {
        return trackId;
    }

    void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    BigDecimal getUnitPrice() {
        return unitPrice;
    }

    void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    Integer getQuantity() {
        return quantity;
    }

    void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

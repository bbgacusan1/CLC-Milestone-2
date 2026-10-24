package edu.gcu.cst339.lab2_chinook_api.invoiceline;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity mapped to the Chinook {@code invoice_line} table.
 *
 * <p>Each invoice line is one item on an invoice: which track was bought, its
 * unit price and the quantity. {@code invoiceId} is a foreign key to
 * {@code invoice.invoice_id} and {@code trackId} is a foreign key to
 * {@code track.track_id}, so this table links invoices to the tracks they
 * contain.</p>
 */
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

    /**
     * No-argument constructor required by JPA. Application code should use the
     * full constructor instead.
     */
    protected InvoiceLine() {
    }

    /**
     * Creates a new, unsaved invoice line. The invoice line ID is assigned by the
     * database when the line is saved.
     *
     * @param invoiceId the ID of an existing invoice this line belongs to
     * @param trackId   the ID of an existing track that was purchased
     * @param unitPrice the price of one unit of the track
     * @param quantity  the number of units purchased
     */
    InvoiceLine(Integer invoiceId, Integer trackId, BigDecimal unitPrice, Integer quantity) {
        this.invoiceId = invoiceId;
        this.trackId = trackId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    /** {@return the database-generated invoice line ID, or {@code null} if not yet saved} */
    Integer getInvoiceLineId() {
        return invoiceLineId;
    }

    /** {@return the ID of the invoice this line belongs to} */
    Integer getInvoiceId() {
        return invoiceId;
    }

    /**
     * Sets the invoice ID.
     *
     * @param invoiceId the ID of an existing invoice
     */
    void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    /** {@return the ID of the purchased track} */
    Integer getTrackId() {
        return trackId;
    }

    /**
     * Sets the track ID.
     *
     * @param trackId the ID of an existing track
     */
    void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    /** {@return the price of one unit of the track} */
    BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the unit price.
     *
     * @param unitPrice the unit price (required, zero or more, up to two decimal places)
     */
    void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /** {@return the number of units purchased} */
    Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the number of units purchased.
     *
     * @param quantity the number of units purchased (required, greater than zero)
     */
    void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

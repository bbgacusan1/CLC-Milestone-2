package edu.gcu.cst339.lab2_chinook_api.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity mapped to the Chinook {@code invoice} table.
 *
 * <p>An invoice records a single purchase: the customer who made it, the date,
 * the billing address and the total amount. {@code customerId} is a foreign key
 * to {@code customer.customer_id}. The individual items on the invoice are
 * stored separately in the {@code invoice_line} table, which refers back to the
 * invoice through {@code invoice_line.invoice_id}.</p>
 *
 * <p>The {@code total} is stored as supplied by the client; it is not
 * recalculated from the invoice lines.</p>
 */
@Entity
@Table(name = "invoice")
class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id", nullable = false)
    private Integer invoiceId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;

    @Column(name = "billing_address", length = 70)
    private String billingAddress;

    @Column(name = "billing_city", length = 40)
    private String billingCity;

    @Column(name = "billing_state", length = 40)
    private String billingState;

    @Column(name = "billing_country", length = 40)
    private String billingCountry;

    @Column(name = "billing_postal_code", length = 10)
    private String billingPostalCode;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    /**
     * No-argument constructor required by JPA. Application code should use the
     * full constructor instead.
     */
    protected Invoice() {
    }

    /**
     * Creates a new, unsaved invoice. The invoice ID is assigned by the database
     * when the invoice is saved.
     *
     * @param customerId        the ID of an existing customer who made the purchase
     * @param invoiceDate       the date and time of the purchase
     * @param billingAddress    the billing street address (at most 70 characters)
     * @param billingCity       the billing city (at most 40 characters)
     * @param billingState      the billing state or province (at most 40 characters)
     * @param billingCountry    the billing country (at most 40 characters)
     * @param billingPostalCode the billing postal code (at most 10 characters)
     * @param total             the invoice total, with up to two decimal places
     */
    Invoice(Integer customerId, LocalDateTime invoiceDate, String billingAddress,
            String billingCity, String billingState, String billingCountry,
            String billingPostalCode, BigDecimal total) {
        this.customerId = customerId;
        this.invoiceDate = invoiceDate;
        this.billingAddress = billingAddress;
        this.billingCity = billingCity;
        this.billingState = billingState;
        this.billingCountry = billingCountry;
        this.billingPostalCode = billingPostalCode;
        this.total = total;
    }

    /** {@return the database-generated invoice ID, or {@code null} if not yet saved} */
    Integer getInvoiceId() {
        return invoiceId;
    }

    /** {@return the ID of the customer who made the purchase} */
    Integer getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID.
     *
     * @param customerId the ID of an existing customer
     */
    void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /** {@return the date and time of the purchase} */
    LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * Sets the date and time of the purchase.
     *
     * @param invoiceDate the date and time of the purchase (required)
     */
    void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /** {@return the billing street address} */
    String getBillingAddress() {
        return billingAddress;
    }

    /**
     * Sets the billing street address.
     *
     * @param billingAddress the billing street address (at most 70 characters)
     */
    void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    /** {@return the billing city} */
    String getBillingCity() {
        return billingCity;
    }

    /**
     * Sets the billing city.
     *
     * @param billingCity the billing city (at most 40 characters)
     */
    void setBillingCity(String billingCity) {
        this.billingCity = billingCity;
    }

    /** {@return the billing state or province} */
    String getBillingState() {
        return billingState;
    }

    /**
     * Sets the billing state or province.
     *
     * @param billingState the billing state or province (at most 40 characters)
     */
    void setBillingState(String billingState) {
        this.billingState = billingState;
    }

    /** {@return the billing country} */
    String getBillingCountry() {
        return billingCountry;
    }

    /**
     * Sets the billing country.
     *
     * @param billingCountry the billing country (at most 40 characters)
     */
    void setBillingCountry(String billingCountry) {
        this.billingCountry = billingCountry;
    }

    /** {@return the billing postal code} */
    String getBillingPostalCode() {
        return billingPostalCode;
    }

    /**
     * Sets the billing postal code.
     *
     * @param billingPostalCode the billing postal code (at most 10 characters)
     */
    void setBillingPostalCode(String billingPostalCode) {
        this.billingPostalCode = billingPostalCode;
    }

    /** {@return the invoice total} */
    BigDecimal getTotal() {
        return total;
    }

    /**
     * Sets the invoice total.
     *
     * @param total the invoice total (required, zero or more, up to two decimal places)
     */
    void setTotal(BigDecimal total) {
        this.total = total;
    }
}

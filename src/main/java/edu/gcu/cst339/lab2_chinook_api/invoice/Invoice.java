package edu.gcu.cst339.lab2_chinook_api.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    protected Invoice() {
    }

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

    Integer getInvoiceId() {
        return invoiceId;
    }

    Integer getCustomerId() {
        return customerId;
    }

    void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    String getBillingAddress() {
        return billingAddress;
    }

    void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    String getBillingCity() {
        return billingCity;
    }

    void setBillingCity(String billingCity) {
        this.billingCity = billingCity;
    }

    String getBillingState() {
        return billingState;
    }

    void setBillingState(String billingState) {
        this.billingState = billingState;
    }

    String getBillingCountry() {
        return billingCountry;
    }

    void setBillingCountry(String billingCountry) {
        this.billingCountry = billingCountry;
    }

    String getBillingPostalCode() {
        return billingPostalCode;
    }

    void setBillingPostalCode(String billingPostalCode) {
        this.billingPostalCode = billingPostalCode;
    }

    BigDecimal getTotal() {
        return total;
    }

    void setTotal(BigDecimal total) {
        this.total = total;
    }
}

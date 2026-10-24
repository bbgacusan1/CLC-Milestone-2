package edu.gcu.cst339.lab2_chinook_api.invoice;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for Chinook invoices.
 *
 * <p>Converts between {@link Invoice} entities and {@link InvoiceDto} objects,
 * throws {@link InvoiceNotFoundException} for unknown IDs, and runs each
 * operation in a transaction.</p>
 */
@Service
class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    /**
     * Creates the service. Spring injects the repository through this constructor.
     *
     * @param invoiceRepository the repository used to read and write invoices
     */
    InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Returns every invoice in the database.
     *
     * @return all invoices as DTOs; an empty list if the table is empty
     */
    @Transactional(readOnly = true)
    List<InvoiceDto> findAll() {
        return invoiceRepository.findAll().stream()
                .map(InvoiceDto::fromEntity)
                .toList();
    }

    /**
     * Returns the invoice with the given ID.
     *
     * @param invoiceId the invoice ID to look up
     * @return the matching invoice as a DTO
     * @throws InvoiceNotFoundException if no invoice has the given ID
     */
    @Transactional(readOnly = true)
    InvoiceDto findById(Integer invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .map(InvoiceDto::fromEntity)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
    }

    /**
     * Saves a new invoice. Any {@code invoiceId} in the DTO is ignored because the
     * database generates the ID.
     *
     * @param invoiceDto the details of the new invoice
     * @return the saved invoice, including its generated ID
     */
    @Transactional
    InvoiceDto create(InvoiceDto invoiceDto) {
        Invoice invoice = new Invoice(
                invoiceDto.customerId(),
                invoiceDto.invoiceDate(),
                invoiceDto.billingAddress(),
                invoiceDto.billingCity(),
                invoiceDto.billingState(),
                invoiceDto.billingCountry(),
                invoiceDto.billingPostalCode(),
                invoiceDto.total()
        );
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return InvoiceDto.fromEntity(savedInvoice);
    }

    /**
     * Replaces every editable field of an existing invoice and saves it.
     *
     * @param invoiceId  the ID of the invoice to update
     * @param invoiceDto the new values; any {@code invoiceId} in the DTO is ignored
     * @return the updated invoice
     * @throws InvoiceNotFoundException if no invoice has the given ID
     */
    @Transactional
    InvoiceDto update(Integer invoiceId, InvoiceDto invoiceDto) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
        invoice.setCustomerId(invoiceDto.customerId());
        invoice.setInvoiceDate(invoiceDto.invoiceDate());
        invoice.setBillingAddress(invoiceDto.billingAddress());
        invoice.setBillingCity(invoiceDto.billingCity());
        invoice.setBillingState(invoiceDto.billingState());
        invoice.setBillingCountry(invoiceDto.billingCountry());
        invoice.setBillingPostalCode(invoiceDto.billingPostalCode());
        invoice.setTotal(invoiceDto.total());
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return InvoiceDto.fromEntity(updatedInvoice);
    }

    /**
     * Deletes the invoice with the given ID.
     *
     * <p>The database rejects the delete if invoice lines still belong to the
     * invoice; the controller reports that case as HTTP 409 Conflict.</p>
     *
     * @param invoiceId the ID of the invoice to delete
     * @throws InvoiceNotFoundException if no invoice has the given ID
     */
    @Transactional
    void delete(Integer invoiceId) {
        if (!invoiceRepository.existsById(invoiceId)) {
            throw new InvoiceNotFoundException(invoiceId);
        }
        invoiceRepository.deleteById(invoiceId);
    }
}

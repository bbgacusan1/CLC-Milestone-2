package edu.gcu.cst339.lab2_chinook_api.invoiceline;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for Chinook invoice lines.
 *
 * <p>Converts between {@link InvoiceLine} entities and {@link InvoiceLineDto}
 * objects, throws {@link InvoiceLineNotFoundException} for unknown IDs, and
 * runs each operation in a transaction.</p>
 */
@Service
class InvoiceLineService {
    private final InvoiceLineRepository invoiceLineRepository;

    /**
     * Creates the service. Spring injects the repository through this constructor.
     *
     * @param invoiceLineRepository the repository used to read and write invoice lines
     */
    InvoiceLineService(InvoiceLineRepository invoiceLineRepository) {
        this.invoiceLineRepository = invoiceLineRepository;
    }

    /**
     * Returns every invoice line in the database.
     *
     * @return all invoice lines as DTOs; an empty list if the table is empty
     */
    @Transactional(readOnly = true)
    List<InvoiceLineDto> findAll() {
        return invoiceLineRepository.findAll().stream()
                .map(InvoiceLineDto::fromEntity)
                .toList();
    }

    /**
     * Returns the invoice line with the given ID.
     *
     * @param invoiceLineId the invoice line ID to look up
     * @return the matching invoice line as a DTO
     * @throws InvoiceLineNotFoundException if no invoice line has the given ID
     */
    @Transactional(readOnly = true)
    InvoiceLineDto findById(Integer invoiceLineId) {
        return invoiceLineRepository.findById(invoiceLineId)
                .map(InvoiceLineDto::fromEntity)
                .orElseThrow(() -> new InvoiceLineNotFoundException(invoiceLineId));
    }

    /**
     * Saves a new invoice line. Any {@code invoiceLineId} in the DTO is ignored
     * because the database generates the ID.
     *
     * @param invoiceLineDto the details of the new invoice line
     * @return the saved invoice line, including its generated ID
     */
    @Transactional
    InvoiceLineDto create(InvoiceLineDto invoiceLineDto) {
        InvoiceLine invoiceLine = new InvoiceLine(
                invoiceLineDto.invoiceId(),
                invoiceLineDto.trackId(),
                invoiceLineDto.unitPrice(),
                invoiceLineDto.quantity()
        );
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(invoiceLine);
        return InvoiceLineDto.fromEntity(savedInvoiceLine);
    }

    /**
     * Replaces every editable field of an existing invoice line and saves it.
     *
     * @param invoiceLineId  the ID of the invoice line to update
     * @param invoiceLineDto the new values; any {@code invoiceLineId} in the DTO is ignored
     * @return the updated invoice line
     * @throws InvoiceLineNotFoundException if no invoice line has the given ID
     */
    @Transactional
    InvoiceLineDto update(Integer invoiceLineId, InvoiceLineDto invoiceLineDto) {
        InvoiceLine invoiceLine = invoiceLineRepository.findById(invoiceLineId)
                .orElseThrow(() -> new InvoiceLineNotFoundException(invoiceLineId));
        invoiceLine.setInvoiceId(invoiceLineDto.invoiceId());
        invoiceLine.setTrackId(invoiceLineDto.trackId());
        invoiceLine.setUnitPrice(invoiceLineDto.unitPrice());
        invoiceLine.setQuantity(invoiceLineDto.quantity());
        InvoiceLine updatedInvoiceLine = invoiceLineRepository.save(invoiceLine);
        return InvoiceLineDto.fromEntity(updatedInvoiceLine);
    }

    /**
     * Deletes the invoice line with the given ID.
     *
     * @param invoiceLineId the ID of the invoice line to delete
     * @throws InvoiceLineNotFoundException if no invoice line has the given ID
     */
    @Transactional
    void delete(Integer invoiceLineId) {
        if (!invoiceLineRepository.existsById(invoiceLineId)) {
            throw new InvoiceLineNotFoundException(invoiceLineId);
        }
        invoiceLineRepository.deleteById(invoiceLineId);
    }
}

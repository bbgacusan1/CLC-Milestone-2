package edu.gcu.cst339.lab2_chinook_api.invoiceline;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class InvoiceLineService {
    private final InvoiceLineRepository invoiceLineRepository;

    InvoiceLineService(InvoiceLineRepository invoiceLineRepository) {
        this.invoiceLineRepository = invoiceLineRepository;
    }

    @Transactional(readOnly = true)
    List<InvoiceLineDto> findAll() {
        return invoiceLineRepository.findAll().stream()
                .map(InvoiceLineDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    InvoiceLineDto findById(Integer invoiceLineId) {
        return invoiceLineRepository.findById(invoiceLineId)
                .map(InvoiceLineDto::fromEntity)
                .orElseThrow(() -> new InvoiceLineNotFoundException(invoiceLineId));
    }

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

    @Transactional
    void delete(Integer invoiceLineId) {
        if (!invoiceLineRepository.existsById(invoiceLineId)) {
            throw new InvoiceLineNotFoundException(invoiceLineId);
        }
        invoiceLineRepository.deleteById(invoiceLineId);
    }
}

package edu.gcu.cst339.lab2_chinook_api.invoice;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional(readOnly = true)
    List<InvoiceDto> findAll() {
        return invoiceRepository.findAll().stream()
                .map(InvoiceDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    InvoiceDto findById(Integer invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .map(InvoiceDto::fromEntity)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
    }

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

    @Transactional
    void delete(Integer invoiceId) {
        if (!invoiceRepository.existsById(invoiceId)) {
            throw new InvoiceNotFoundException(invoiceId);
        }
        invoiceRepository.deleteById(invoiceId);
    }
}

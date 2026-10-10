package edu.gcu.cst339.lab2_chinook_api.invoice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    private static final LocalDateTime DATE = LocalDateTime.of(2021, 1, 1, 0, 0);

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    private Invoice invoice(int id, int customerId, String total) {
        Invoice invoice = new Invoice(customerId, DATE, "Theodor-Heuss-Straße 22", "Stuttgart",
                null, "Germany", "70174", new BigDecimal(total));
        ReflectionTestUtils.setField(invoice, "invoiceId", id);
        return invoice;
    }

    private InvoiceDto dto(Integer id, Integer customerId, String total) {
        return new InvoiceDto(id, customerId, DATE, "123 Test St", "Phoenix", "AZ", "USA", "85001",
                new BigDecimal(total));
    }

    @Test
    void findAll_returnsAllInvoicesAsDtos() {
        when(invoiceRepository.findAll())
                .thenReturn(List.of(invoice(1, 2, "1.98"), invoice(2, 4, "3.96")));

        List<InvoiceDto> result = invoiceService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).customerId()).isEqualTo(2);
        assertThat(result.get(1).total()).isEqualByComparingTo("3.96");
    }

    @Test
    void findById_existingId_returnsDtoWithAllFields() {
        when(invoiceRepository.findById(1)).thenReturn(Optional.of(invoice(1, 2, "1.98")));

        InvoiceDto result = invoiceService.findById(1);

        assertThat(result.invoiceId()).isEqualTo(1);
        assertThat(result.customerId()).isEqualTo(2);
        assertThat(result.invoiceDate()).isEqualTo(DATE);
        assertThat(result.billingCity()).isEqualTo("Stuttgart");
        assertThat(result.billingState()).isNull();
        assertThat(result.billingCountry()).isEqualTo("Germany");
        assertThat(result.billingPostalCode()).isEqualTo("70174");
        assertThat(result.total()).isEqualByComparingTo("1.98");
    }

    @Test
    void findById_missingId_throwsNotFound() {
        when(invoiceRepository.findById(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> invoiceService.findById(99999))
                .isInstanceOf(InvoiceNotFoundException.class)
                .hasMessageContaining("99999");
    }

    @Test
    void create_savesInvoiceAndReturnsDtoWithGeneratedId() {
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice toSave = invocation.getArgument(0);
            ReflectionTestUtils.setField(toSave, "invoiceId", 413);
            return toSave;
        });

        InvoiceDto result = invoiceService.create(dto(null, 1, "9.99"));

        assertThat(result.invoiceId()).isEqualTo(413);
        assertThat(result.customerId()).isEqualTo(1);
        assertThat(result.billingCity()).isEqualTo("Phoenix");
        assertThat(result.total()).isEqualByComparingTo("9.99");
        verify(invoiceRepository).save(any(Invoice.class));
    }

    @Test
    void update_existingId_changesFieldsButNotId() {
        Invoice existing = invoice(5, 2, "1.98");
        when(invoiceRepository.findById(5)).thenReturn(Optional.of(existing));
        when(invoiceRepository.save(existing)).thenReturn(existing);

        InvoiceDto result = invoiceService.update(5, dto(999, 10, "12.50"));

        assertThat(result.invoiceId()).isEqualTo(5);
        assertThat(result.customerId()).isEqualTo(10);
        assertThat(result.billingCity()).isEqualTo("Phoenix");
        assertThat(result.billingState()).isEqualTo("AZ");
        assertThat(result.total()).isEqualByComparingTo("12.50");
        assertThat(existing.getCustomerId()).isEqualTo(10);
    }

    @Test
    void update_missingId_throwsNotFound() {
        when(invoiceRepository.findById(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> invoiceService.update(99999, dto(null, 1, "1.00")))
                .isInstanceOf(InvoiceNotFoundException.class);
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    void delete_existingId_deletesInvoice() {
        when(invoiceRepository.existsById(5)).thenReturn(true);

        invoiceService.delete(5);

        verify(invoiceRepository).deleteById(5);
    }

    @Test
    void delete_missingId_throwsNotFoundAndDeletesNothing() {
        when(invoiceRepository.existsById(99999)).thenReturn(false);

        assertThatThrownBy(() -> invoiceService.delete(99999))
                .isInstanceOf(InvoiceNotFoundException.class);
        verify(invoiceRepository, never()).deleteById(any());
    }
}

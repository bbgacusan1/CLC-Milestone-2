package edu.gcu.cst339.lab2_chinook_api.invoiceline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class InvoiceLineServiceTest {

    @Mock
    private InvoiceLineRepository invoiceLineRepository;

    @InjectMocks
    private InvoiceLineService invoiceLineService;

    private InvoiceLine invoiceLine(int id, int invoiceId, int trackId, String unitPrice, int quantity) {
        InvoiceLine invoiceLine = new InvoiceLine(invoiceId, trackId, new BigDecimal(unitPrice), quantity);
        ReflectionTestUtils.setField(invoiceLine, "invoiceLineId", id);
        return invoiceLine;
    }

    @Test
    void findAll_returnsAllInvoiceLinesAsDtos() {
        when(invoiceLineRepository.findAll())
                .thenReturn(List.of(invoiceLine(1, 1, 2, "0.99", 1), invoiceLine(2, 1, 4, "0.99", 1)));

        List<InvoiceLineDto> result = invoiceLineService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).trackId()).isEqualTo(2);
        assertThat(result.get(1).invoiceLineId()).isEqualTo(2);
    }

    @Test
    void findById_existingId_returnsDtoWithAllFields() {
        when(invoiceLineRepository.findById(1)).thenReturn(Optional.of(invoiceLine(1, 1, 2, "0.99", 1)));

        InvoiceLineDto result = invoiceLineService.findById(1);

        assertThat(result.invoiceLineId()).isEqualTo(1);
        assertThat(result.invoiceId()).isEqualTo(1);
        assertThat(result.trackId()).isEqualTo(2);
        assertThat(result.unitPrice()).isEqualByComparingTo("0.99");
        assertThat(result.quantity()).isEqualTo(1);
    }

    @Test
    void findById_missingId_throwsNotFound() {
        when(invoiceLineRepository.findById(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> invoiceLineService.findById(99999))
                .isInstanceOf(InvoiceLineNotFoundException.class)
                .hasMessageContaining("99999");
    }

    @Test
    void create_savesInvoiceLineAndReturnsDtoWithGeneratedId() {
        when(invoiceLineRepository.save(any(InvoiceLine.class))).thenAnswer(invocation -> {
            InvoiceLine toSave = invocation.getArgument(0);
            ReflectionTestUtils.setField(toSave, "invoiceLineId", 2241);
            return toSave;
        });

        InvoiceLineDto result = invoiceLineService.create(
                new InvoiceLineDto(null, 1, 3, new BigDecimal("1.99"), 2));

        assertThat(result.invoiceLineId()).isEqualTo(2241);
        assertThat(result.invoiceId()).isEqualTo(1);
        assertThat(result.trackId()).isEqualTo(3);
        assertThat(result.unitPrice()).isEqualByComparingTo("1.99");
        assertThat(result.quantity()).isEqualTo(2);
        verify(invoiceLineRepository).save(any(InvoiceLine.class));
    }

    @Test
    void update_existingId_changesFieldsButNotId() {
        InvoiceLine existing = invoiceLine(5, 1, 2, "0.99", 1);
        when(invoiceLineRepository.findById(5)).thenReturn(Optional.of(existing));
        when(invoiceLineRepository.save(existing)).thenReturn(existing);

        InvoiceLineDto result = invoiceLineService.update(5,
                new InvoiceLineDto(999, 2, 10, new BigDecimal("1.99"), 3));

        assertThat(result.invoiceLineId()).isEqualTo(5);
        assertThat(result.invoiceId()).isEqualTo(2);
        assertThat(result.trackId()).isEqualTo(10);
        assertThat(result.unitPrice()).isEqualByComparingTo("1.99");
        assertThat(result.quantity()).isEqualTo(3);
        assertThat(existing.getQuantity()).isEqualTo(3);
    }

    @Test
    void update_missingId_throwsNotFound() {
        when(invoiceLineRepository.findById(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> invoiceLineService.update(99999,
                new InvoiceLineDto(null, 1, 2, new BigDecimal("0.99"), 1)))
                .isInstanceOf(InvoiceLineNotFoundException.class);
        verify(invoiceLineRepository, never()).save(any());
    }

    @Test
    void delete_existingId_deletesInvoiceLine() {
        when(invoiceLineRepository.existsById(5)).thenReturn(true);

        invoiceLineService.delete(5);

        verify(invoiceLineRepository).deleteById(5);
    }

    @Test
    void delete_missingId_throwsNotFoundAndDeletesNothing() {
        when(invoiceLineRepository.existsById(99999)).thenReturn(false);

        assertThatThrownBy(() -> invoiceLineService.delete(99999))
                .isInstanceOf(InvoiceLineNotFoundException.class);
        verify(invoiceLineRepository, never()).deleteById(any());
    }
}

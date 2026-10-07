package edu.gcu.cst339.lab2_chinook_api.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer(int id, String first, String last, String email) {
        Customer c = new Customer();
        ReflectionTestUtils.setField(c, "customerId", id);
        c.setFirstName(first);
        c.setLastName(last);
        c.setEmail(email);
        return c;
    }

    private CustomerDto dto(String first, String last, String email, String city) {
        return new CustomerDto(null, first, last, null, null, city, null, null,
                null, null, null, email, null);
    }

    @Test
    void findAll_returnsAllCustomersAsDtos() {
        when(customerRepository.findAll()).thenReturn(List.of(
                customer(1, "Luís", "Gonçalves", "luisg@embraer.com.br"),
                customer(2, "Leonie", "Köhler", "leonekohler@surfeu.de")));

        List<CustomerDto> result = customerService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).firstName()).isEqualTo("Luís");
        assertThat(result.get(1).customerId()).isEqualTo(2);
    }

    @Test
    void findById_existingId_returnsDto() {
        when(customerRepository.findById(1))
                .thenReturn(Optional.of(customer(1, "Luís", "Gonçalves", "luisg@embraer.com.br")));

        CustomerDto result = customerService.findById(1);

        assertThat(result.customerId()).isEqualTo(1);
        assertThat(result.email()).isEqualTo("luisg@embraer.com.br");
    }

    @Test
    void findById_missingId_throwsNotFound() {
        when(customerRepository.findById(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findById(99999))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void create_copiesFieldsAndReturnsDtoWithGeneratedId() {
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer toSave = invocation.getArgument(0);
            ReflectionTestUtils.setField(toSave, "customerId", 60);
            return toSave;
        });

        CustomerDto result = customerService.create(
                dto("Test", "Customer", "test.customer@example.com", "Phoenix"));

        assertThat(result.customerId()).isEqualTo(60);
        assertThat(result.firstName()).isEqualTo("Test");
        assertThat(result.city()).isEqualTo("Phoenix");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void update_existingId_changesFieldsButNotId() {
        Customer existing = customer(7, "Old", "Name", "old@example.com");
        when(customerRepository.findById(7)).thenReturn(Optional.of(existing));

        CustomerDto result = customerService.update(7,
                dto("New", "Name", "new@example.com", "Glendale"));

        assertThat(result.customerId()).isEqualTo(7);
        assertThat(result.firstName()).isEqualTo("New");
        assertThat(existing.getCity()).isEqualTo("Glendale");
        assertThat(existing.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    void update_missingId_throwsNotFound() {
        when(customerRepository.findById(99999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.update(99999,
                dto("X", "Y", "x@example.com", null)))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void delete_existingId_deletesCustomer() {
        when(customerRepository.existsById(7)).thenReturn(true);

        customerService.delete(7);

        verify(customerRepository).deleteById(7);
    }

    @Test
    void delete_missingId_throwsNotFoundAndDeletesNothing() {
        when(customerRepository.existsById(99999)).thenReturn(false);

        assertThatThrownBy(() -> customerService.delete(99999))
                .isInstanceOf(CustomerNotFoundException.class);
        verify(customerRepository, never()).deleteById(any());
    }
}
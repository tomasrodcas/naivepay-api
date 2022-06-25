package cl.ufro.dci.naive.service.customers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import cl.ufro.dci.naive.domain.Customer;
import cl.ufro.dci.naive.exceptions.ApiForbiddenException;
import cl.ufro.dci.naive.repository.AccessRepository;
import cl.ufro.dci.naive.repository.CustomerRepository;
import cl.ufro.dci.naive.security.authentication.AuthenticationFacade;
import cl.ufro.dci.naive.service.customer.CustomerService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private CustomerService service;

    @MockBean
    private CustomerRepository repository;

    @MockBean
    private AccessRepository accessRepository;

    @MockBean
    private AuthenticationFacade authentication;

    private static ObjectMapper mapper = new ObjectMapper();

    private static final File customersFile = Paths
            .get("src", "test", "resources", "service", "customers", "customers.json").toFile();

    private static List<Customer> customers;

    @BeforeAll
    static void setup() throws StreamReadException, DatabindException, IOException {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        customers = mapper.readValue(customersFile, new TypeReference<List<Customer>>() {
        });

    }

    @Test
    void getCustomers() {
        when(repository.findAll()).thenReturn(customers);
        assertEquals(customers, service.getCustomers());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void getCustomerById(int index) {
        when(repository.findById((long) index + 1)).thenReturn(Optional.of(customers.get(index)));
        when(repository.findBycusAccess_accId(any(Long.class))).thenReturn(Optional.of(customers.get(index)));
        when(authentication.getAuthenticatedAccessId()).thenReturn(1L);
        when(authentication.getAuthenticatedRole()).thenReturn(1);
        assertEquals(customers.get(index), service.getCustomerById((long) index + 1));

    }

    @Test
    @DisplayName("Should Throw Forbidden Exception - Customer doesnt have ownership")
    void shouldThrowForbiddenExceptionFetching() {
        when(repository.findById(1L)).thenReturn(Optional.of(customers.get(0)));
        when(repository.findBycusAccess_accId(1L)).thenReturn(Optional.of(customers.get(1)));
        when(authentication.getAuthenticatedAccessId()).thenReturn(1L);
        when(authentication.getAuthenticatedRole()).thenReturn(0);
        assertThrows(ApiForbiddenException.class, () -> {
            service.getCustomerById(1L);
        });
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void createCustomer(int index) {
        when(repository.save(customers.get(index))).thenReturn(customers.get(index));
        assertEquals(customers.get(index), service.createCustomer(customers.get(index)));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void updateCustomer(int index) {
        when(repository.save(any(Customer.class))).thenReturn(customers.get(index));
        when(repository.findById((long) index + 1)).thenReturn(Optional.of(customers.get(index)));
        when(repository.findBycusAccess_accId(1L)).thenReturn(Optional.of(customers.get(index)));
        when(authentication.getAuthenticatedAccessId()).thenReturn(1L);
        assertEquals(customers.get(index), service.updateCustomer(customers.get(index)));

    }

    @Test
    void shouldThrowForbiddenExceptionUpdating() {
        when(repository.save(any(Customer.class))).thenReturn(customers.get(0));
        when(repository.findById(1L)).thenReturn(Optional.of(customers.get(0)));
        when(repository.findBycusAccess_accId(1L)).thenReturn(Optional.of(customers.get(1)));
        when(authentication.getAuthenticatedAccessId()).thenReturn(1L);
        when(authentication.getAuthenticatedRole()).thenReturn(0);
        when(authentication.getAuthenticatedRole()).thenReturn(0);

        assertThrows(ApiForbiddenException.class, () -> {
            service.updateCustomer(customers.get(0));
        });
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void deleteCustomer(int index) {
        when(repository.findById((long) index + 1)).thenReturn(Optional.of(customers.get(index)));
        when(repository.findBycusAccess_accId(1L)).thenReturn(Optional.of(customers.get(index)));
        when(authentication.getAuthenticatedAccessId()).thenReturn(1L);
        when(authentication.getAuthenticatedRole()).thenReturn(0);
        service.deleteCustomer((long) index + 1);
    }

}

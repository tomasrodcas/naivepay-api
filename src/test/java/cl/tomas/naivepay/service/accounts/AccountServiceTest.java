package cl.tomas.naivepay.service.accounts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
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

import cl.tomas.naivepay.domain.Account;
import cl.tomas.naivepay.domain.Customer;
import cl.tomas.naivepay.exceptions.ApiForbiddenException;
import cl.tomas.naivepay.repository.AccessRepository;
import cl.tomas.naivepay.repository.AccountRepository;
import cl.tomas.naivepay.repository.CustomerRepository;
import cl.tomas.naivepay.security.authentication.AuthenticationFacade;
import cl.tomas.naivepay.service.account.AccountService;
import cl.tomas.naivepay.service.customer.CustomerService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    AccountService service;
    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private AccessRepository accessRepository;

    @MockBean
    private AuthenticationFacade authentication;

    @MockBean
    private AccountRepository repository;

    private static ObjectMapper mapper = new ObjectMapper();

    private static final File accountsFile = Paths
            .get("src", "test", "resources", "service", "accounts", "accounts.json").toFile();
    private static final File customersFile = Paths
            .get("src", "test", "resources", "service", "accounts", "customers.json").toFile();

    private static List<Account> accounts;
    private static List<Customer> customers;

    @BeforeAll
    static void setup() throws StreamReadException, DatabindException, IOException {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        accounts = mapper.readValue(accountsFile, new TypeReference<List<Account>>() {
        });
        customers = mapper.readValue(customersFile, new TypeReference<List<Customer>>() {
        });

    }

    @Test
    void getAccounts() {
        when(repository.findAll()).thenReturn(accounts);
        assertEquals(accounts, service.getAccounts());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void getAccountByIdAdmin(int index) {
        when(repository.findById((long) index + 1)).thenReturn(Optional.of(accounts.get(index)));
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customers.get(0)));
        when(customerRepository.findBycusAccess_accId(any(Long.class))).thenReturn(Optional.of(customers.get(0)));
        when(authentication.getAuthenticatedAccessId()).thenReturn(1L);
        when(authentication.getAuthenticatedRole()).thenReturn(1);
        assertEquals(accounts.get(index), service.getAccountById((long) index + 1));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void getAccountByIdAccountOwner(int index) {
        when(repository.findById((long) index + 1)).thenReturn(Optional.of(accounts.get(index)));
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customers.get(index)));
        when(customerRepository.findBycusAccess_accId(any(Long.class))).thenReturn(Optional.of(customers.get(index)));
        when(authentication.getAuthenticatedAccessId()).thenReturn((long) index + 1);
        when(authentication.getAuthenticatedRole()).thenReturn(0);
        assertEquals(accounts.get(index), service.getAccountById((long) index + 1));

    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3 })
    void shouldReturnForbiddenGetAccountNoOwnership(int index) {
        when(repository.findById((long) index + 1)).thenReturn(Optional.of(accounts.get(index)));
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customers.get(0)));
        when(customerRepository.findBycusAccess_accId(any(Long.class))).thenReturn(Optional.of(customers.get(0)));
        when(authentication.getAuthenticatedAccessId()).thenReturn(1L);
        when(authentication.getAuthenticatedRole()).thenReturn(0);
        assertThrows(ApiForbiddenException.class, () -> service.getAccountById((long) index + 1));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void createAccount(int index) {
        when(repository.save(any(Account.class))).thenReturn(accounts.get(index));
        assertEquals(accounts.get(index), service.createAccount(accounts.get(index)));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void updateAccountAsAdmin(int index) {
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(accounts.get(index)));
        when(repository.save(any(Account.class))).thenReturn(accounts.get(index));
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customers.get(0)));
        when(customerRepository.findBycusAccess_accId(any(Long.class))).thenReturn(Optional.of(customers.get(0)));
        when(authentication.getAuthenticatedAccessId()).thenReturn(1L);
        when(authentication.getAuthenticatedRole()).thenReturn(1);
        assertEquals(accounts.get(index), service.updateAccount(accounts.get(index)));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void updateAccountAsOwner(int index) {
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(accounts.get(index)));
        when(repository.save(any(Account.class))).thenReturn(accounts.get(index));
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customers.get(index)));
        when(customerRepository.findBycusAccess_accId(any(Long.class))).thenReturn(Optional.of(customers.get(index)));
        when(authentication.getAuthenticatedAccessId()).thenReturn((long) index + 1);
        when(authentication.getAuthenticatedRole()).thenReturn(0);
        assertEquals(accounts.get(index), service.updateAccount(accounts.get(index)));
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3 })
    void shouldThrowForbiddenUpdateAccountWithoutOwnership(int index) {
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(accounts.get(index)));
        when(repository.save(any(Account.class))).thenReturn(accounts.get(index));
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customers.get(0)));
        when(customerRepository.findBycusAccess_accId(any(Long.class))).thenReturn(Optional.of(customers.get(0)));
        when(authentication.getAuthenticatedAccessId()).thenReturn(1L);
        when(authentication.getAuthenticatedRole()).thenReturn(0);
        assertThrows(ApiForbiddenException.class, () -> service.updateAccount(accounts.get(index)));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void deleteAccounts(int index) {
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(accounts.get(index)));
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customers.get(index)));
        when(customerRepository.findBycusAccess_accId(any(Long.class))).thenReturn(Optional.of(customers.get(index)));
        when(authentication.getAuthenticatedAccessId()).thenReturn((long) index + 1);
        when(authentication.getAuthenticatedRole()).thenReturn(0);
        assertTrue(service.deleteAccount((long) index + 1));
    }

}

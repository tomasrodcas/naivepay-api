package cl.ufro.dci.naive.service.transaction;

import cl.ufro.dci.naive.domain.Account;
import cl.ufro.dci.naive.domain.Transaction;
import cl.ufro.dci.naive.domain.TransactionState;
import cl.ufro.dci.naive.repository.AccountRepository;
import cl.ufro.dci.naive.repository.TransactionRepository;
import cl.ufro.dci.naive.repository.TransactionStateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShowInformationTransfersTest {

    private TransactionService transactionService;

    private static final File TRANSACTIONS_STATE = Paths.get("src", "test", "resources", "transaction", "TransfersState.json").toFile();

    private TransactionState[] transactionState;

    private List<Transaction> list;
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionStateRepository transactionStateRepository;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            transactionState = mapper.readValue(TRANSACTIONS_STATE, TransactionState[].class);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        transactionStateRepository.saveAll(Arrays.asList(transactionState));
        transactionService.setTransactionStateRepository(transactionStateRepository);
        transactionService.setTransactionRepository(transactionRepository);
    }

    @AfterEach
    void tearDown() {
        transactionStateRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("CP01-01: Base case, transfers with transfer status 'Unpaid' are shown")
    void showTransferUnpaidCP01_01() {

        list = transactionRepository.findAllByTraOriginAccount("1")
                .orElseThrow().stream().filter(t -> t.getTraTransactionState().getTrsName().equals("Unpaid")).collect(Collectors.toList());

        Account account = new Account();
        account.setAccId(1L);
        account.setAccAmount(100000);

        assertEquals(list, transactionService.transactionsPendingApproval(account));
    }

    @Test
    @Order(2)
    @DisplayName("CP04-01: Base case, transfers with transfer status 'Paid' are shown")
    void showTransferConfirmedCP04_01() {
        list = transactionRepository.findAllByTraOriginAccount("1")
                .orElseThrow().stream().filter(t -> t.getTraTransactionState().getTrsName().equals("Paid")).collect(Collectors.toList());

        Account account = new Account();
        account.setAccId(1L);
        account.setAccAmount(100000);

        assertEquals(list, transactionService.expenses(account));
    }

    @Test
    @Order(3)
    @DisplayName("CP06-01: Base case, return all recorded transactions")
    void showAllMovementsCP06_01() {
        Account account = new Account();
        account.setAccId(1L);
        account.setAccAmount(100000);
        accountRepository.save(account);
        transactionService.setAccountRepository(accountRepository);

        var accInfo = accountRepository.findById(account.getAccId()).orElseThrow();

        assertEquals(accInfo, transactionService.seeStatus(account));
    }
}

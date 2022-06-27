package cl.tomas.naivepay.service.transaction;

import cl.tomas.naivepay.infrastructure.models.Account;
import cl.tomas.naivepay.infrastructure.models.Transaction;
import cl.tomas.naivepay.infrastructure.models.TransactionState;
import cl.tomas.naivepay.domain.exceptions.ApiRequestException;
import cl.tomas.naivepay.infrastructure.repository.AccountRepository;
import cl.tomas.naivepay.infrastructure.repository.TransactionRepository;
import cl.tomas.naivepay.infrastructure.repository.TransactionStateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransfersTest {

    private TransactionService transactionService;

    private static final File ACCOUNTS = Paths.get("src", "test", "resources", "transaction", "Accounts.json").toFile();
    private static final File TRANSACTIONS = Paths.get("src", "test", "resources", "transaction", "Transfers.json").toFile();
    private static final File TRANSACTIONS_STATE = Paths.get("src", "test", "resources", "transaction", "TransfersState.json").toFile();

    private Transaction[] transactions;
    private Account[] accounts;

    private TransactionState[] transactionState;

    @Autowired
    TransactionRepository transactionRepository;

//    @Autowired
//    AccountRepository accountRepository;
//
//    @Autowired
//    TransactionStateRepository transactionStateRepository;
//
//    @BeforeEach
//    void setUp() {
//        transactionService = new TransactionService();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        try {
//            transactionState = mapper.readValue(TRANSACTIONS_STATE, TransactionState[].class);
//            accounts = mapper.readValue(ACCOUNTS, Account[].class);
//            transactions = mapper.readValue(TRANSACTIONS, Transaction[].class);
//        } catch (Exception e) {
//            System.out.println("Error: " + e);
//        }
//        accountRepository.saveAll(Arrays.asList(accounts));
//        transactionState[0].setTrsId(1L);
//        transactionService.setTransactionStateRepository(transactionStateRepository);
//        transactionService.setAccountRepository(accountRepository);
//        transactionService.setTransactionStateRepository(transactionStateRepository);
//    }
//
//    @AfterEach
//    void tearDown() {
//        accountRepository.deleteAll();
//        transactionRepository.deleteAll();
//        transactionStateRepository.deleteAll();
//    }
//
//
//    @Test
//    @DisplayName("CP03-01: Base case, transfer successful")
//    @Order(1)
//    void TransfersCP03_01() {
//        accounts[0].setAccAmount(1000000);
//        accountRepository.save(accounts[0]);
//        transactionStateRepository.save(transactionState[0]);
//        transactionService.setTransactionRepository(transactionRepository);
//        transactionService.setAccountRepository(accountRepository);
//        Transaction expected = new Transaction();
//        expected.setTraId(1L);
//        expected.setTraAmount(transactions[0].getTraAmount());
//        expected.setTraOriginAccount("1");
//        expected.setTraTransactionState(transactions[0].getTraTransactionState());
//        expected.setTraDestinationAccount("2");
//
//        assertEquals(expected, transactionService.transferAmount(transactions[0]));
//    }
//
//    @Test
//    @DisplayName("CP03-02: Both accounts are equal")
//    void TransfersCP03_02() {
//        assertThrows(
//                ApiRequestException.class,
//                () -> transactionService.transferAmount(transactions[1]),
//                "Accounts must be different"
//        );
//    }
//
//    @Test
//    @DisplayName("CP03-03: Origin account not exist")
//    void TransfersCP03_03() {
//        assertThrows(
//                ApiRequestException.class,
//                () -> transactionService.transferAmount(transactions[2]),
//                "Origin account not exist"
//        );
//    }
//
//    @Test
//    @DisplayName("CP03-04: Origin account not declared")
//    void TransfersCP03_04() {
//        assertThrows(
//                ApiRequestException.class,
//                () -> transactionService.transferAmount(transactions[3]),
//                "Insufficient data"
//        );
//    }
//
//    @Test
//    @DisplayName("CP03-05: Destination account not exist")
//    void TransfersCP03_05() {
//        assertThrows(
//                ApiRequestException.class,
//                () -> transactionService.transferAmount(transactions[4]),
//                "Destination account not exist"
//        );
//    }
//
//    @Test
//    @DisplayName("CP03-06: Destination account not declared")
//    void TransfersCP03_06() {
//        assertThrows(
//                ApiRequestException.class,
//                () -> transactionService.transferAmount(transactions[5]),
//                "Insufficient data"
//        );
//    }
//
//    @Test
//    @DisplayName("CP03-07: Amount insufficient of origin account")
//    void TransfersCP03_07() {
//        assertThrows(
//                ApiRequestException.class,
//                () -> transactionService.transferAmount(transactions[6]),
//                "Amount of origin account is insufficient"
//        );
//    }
//
//    @Test
//    @DisplayName("CP03-08: Transaction has already been completed")
//    void TransfersCP03_08() {
//        assertThrows(
//                ApiRequestException.class,
//                () -> transactionService.transferAmount(transactions[7]),
//                "Transaction has already been completed"
//        );
//    }
//
//    @Test
//    @DisplayName("CP03-09: Transfer status not declared")
//    void TransfersCP03_09() {
//        assertThrows(
//                ApiRequestException.class,
//                () -> transactionService.transferAmount(transactions[8]),
//                "Insufficient data"
//        );
//    }
//
//    @Test
//    @DisplayName("CP03-10: Transfer status ID was not found")
//    void TransfersCP03_10() {
//        assertThrows(
//                ApiRequestException.class,
//                () -> transactionService.transferAmount(transactions[9]),
//                "ID of transaction state not found"
//        );
//    }
//
//    @Test
//    @DisplayName("CP05-01: Base case, increase amount successfully")
//    void increaseAmountCP05_01() {
//        accountRepository.save(accounts[0]);
//        transactionService.setAccountRepository(accountRepository);
//
//        transactionService.setTransactionRepository(transactionRepository);
//        int amount = 10000;
//        Account expected = new Account();
//        expected.setAccId(accounts[1].getAccId());
//        expected.setAccAmount(accounts[1].getAccAmount() +  amount);
//
//        assertEquals(expected, transactionService.increaseAmount(accounts[1]));
//    }
//
//    @Test
//    @DisplayName("CP05-02: Invalid data entered")
//    void increaseAmountCP05_02() {
//        Account expected = new Account();
//        expected.setAccAmount(0);
//        //not entered accId
//        assertThrows(
//                ApiRequestException.class,
//                () -> transactionService.increaseAmount(expected),
//                "Invalid data"
//        );
//    }
//
//    @Test
//    @DisplayName("CP05-03: Account not exist")
//    void increaseAmountCP05_03() {
//        Account expected = new Account();
//        //accID should not be 0
//        expected.setAccId(0L);
//        expected.setAccAmount(0);
//        assertThrows(
//                ApiRequestException.class,
//                () -> transactionService.increaseAmount(expected),
//                "Account not exist"
//        );
//    }
}
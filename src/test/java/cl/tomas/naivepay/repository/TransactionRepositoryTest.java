package cl.tomas.naivepay.repository;

import cl.tomas.naivepay.infrastructure.models.Account;
import cl.tomas.naivepay.infrastructure.models.Transaction;
import cl.tomas.naivepay.infrastructure.models.TransactionState;
import cl.tomas.naivepay.infrastructure.repository.AccountRepository;
import cl.tomas.naivepay.infrastructure.repository.TransactionRepository;
import cl.tomas.naivepay.infrastructure.repository.TransactionStateRepository;
import cl.tomas.naivepay.repository.util.UtilTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransactionRepositoryTest {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionStateRepository transactionStateRepository;

    @BeforeEach
    void initEach() throws IOException {
        Transaction[] transactions = new ObjectMapper().readValue(UtilTest.DATA_JSON_TRANSACTIONS, Transaction[].class);
        Account[] accounts = new ObjectMapper().readValue(UtilTest.DATA_JSON_ACCOUNTS, Account[].class);
        TransactionState[] transactionStates = new ObjectMapper().readValue(UtilTest.DATA_JSON_TRANSACTIONSTATES, TransactionState[].class);
        transactions[0].setTraAccount(accounts[0]);
        transactions[0].setTraTransactionState(transactionStates[0]);
        transactions[1].setTraAccount(accounts[0]);
        transactions[1].setTraTransactionState(transactionStates[0]);
        transactions[2].setTraTransactionState(transactionStates[2]);

        Arrays.stream(transactions).forEach(transactionRepository::save);
        Arrays.stream(accounts).forEach(accountRepository::save);
        Arrays.stream(transactionStates).forEach(transactionStateRepository::save);
    }

    @AfterEach
    public void cleanUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        transactionStateRepository.deleteAll();
    }

    @Test
    @Order(2)
    @DisplayName("-CP1- Test Transaction saved successfully")
    void testTransactionSavedSuccesfully() {
        Transaction newTransaction = new Transaction();
        newTransaction.setTraAmount(30000);

        Transaction savedTransaction = transactionRepository.save(newTransaction);

        Assertions.assertNotNull(savedTransaction, "Transaction should be saved");
        Assertions.assertNotNull(savedTransaction.getTraId(), "Transaction should have an id when saved");
        Assertions.assertEquals(newTransaction.getTraAmount(), savedTransaction.getTraAmount());
    }

    @Test
    @Order(1)
    @DisplayName("-CP2- Test Transaction deleted succesfully")
    void testTransactionDeleted() {
        int count = (int) transactionRepository.count();

        transactionRepository.deleteById(1L);

        Assertions.assertEquals((count - 1), (int) transactionRepository.count());
    }

    @Test
    @Order(3)
    @DisplayName("-CP3- Test transaction find by Account")
    void testTransactionFindByAccount() {
        Optional<Account> account = accountRepository.findByAccNum(324512);

        List<Transaction> transactions = transactionRepository.findTransactionsByTraAccount(account.get());

        Assertions.assertEquals(2, transactions.size());
    }

    @Test
    @Order(4)
    @DisplayName("-CP4- Test transaction find by TransactionState")
    void testTransactionFindByState() {
        Optional<TransactionState> transactionState = transactionStateRepository.findByTrsName("Realizada");

        List<Transaction> transactions = transactionRepository.findTransactionsByTraTransactionState(transactionState.get());

        Assertions.assertEquals(2, transactions.size());
    }
}




package cl.ufro.dci.naive.controller.transaction;

import cl.ufro.dci.naive.domain.Account;
import cl.ufro.dci.naive.domain.Transaction;
import cl.ufro.dci.naive.domain.TransactionState;
import cl.ufro.dci.naive.service.transaction.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;
    private TransactionController transactionController;

    private Transaction mockTransaction;
    private Account mockAccount;

    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        transactionService = Mockito.mock(TransactionService.class);
        var mockTransactionState = Mockito.mock(TransactionState.class);
        var mockTransactionStateDestination = Mockito.mock((TransactionState.class));
        transactions = new ArrayList<>();
        transactionController = new TransactionController(transactionService);

        mockTransactionState.setTrsId(1L);
        mockTransactionState.setTrsName("Unpaid");
        mockTransactionStateDestination.setTrsId(2L);
        mockTransactionStateDestination.setTrsName("Unpaid");

        mockTransaction = new Transaction();
        mockTransaction.setTraId(1L);
        mockTransaction.setTraOriginAccount("1");
        mockTransaction.setTraDestinationAccount("2");
        mockTransaction.setTraAmount(100);
        mockTransaction.setTraTransactionState(mockTransactionState);

        mockAccount = new Account();
        mockAccount.setAccId(1L);
        mockAccount.setAccAmount(0);

        var mockTransactionDestination = new Transaction();
        mockTransactionDestination.setTraId(2L);
        mockTransactionDestination.setTraOriginAccount("2");
        mockTransactionDestination.setTraDestinationAccount("1");
        mockTransactionDestination.setTraAmount(200);
        mockTransactionDestination.setTraTransactionState(mockTransactionStateDestination);
    }

    @Test
    @DisplayName("CP07-01: Test Transfer amount: @Put, /transfer-amount, @RequestBody Transaction transaction")
    void transferAmountControllerTest() {
        Transaction transaction = mockTransaction;
        Mockito.when(transactionService.transferAmount(mockTransaction)).thenReturn(mockTransaction);
        ResponseEntity<Transaction> controllerResponse = transactionController.transferAmount(transaction);
        assertEquals(controllerResponse.getBody(), mockTransaction);
    }

    @Test
    @DisplayName("CP07-02: Test increase amount: @Put, /increase-amount, @RequestBody Account account")
    void increaseAmountControllerTest() {
        Account account = mockAccount;
        Mockito.when(transactionService.increaseAmount(mockAccount)).thenReturn(mockAccount);
        ResponseEntity<Account> controllerResponse = transactionController.increaseAmount(account);
        assertEquals(controllerResponse.getBody(), mockAccount);
    }

    @Test
    @DisplayName("CP07-03: Test return List incomes: @Get, /incomes, @RequestBody Account account")
    void incomesControllerTest() {
        Mockito.when(transactionService.incomes(mockAccount)).thenReturn(transactions);
        ResponseEntity<List<Transaction>> controllerResponse = transactionController.incomes(mockAccount);
        assertEquals(controllerResponse.getBody(), transactions);
    }

    @Test
    @DisplayName("CP07-04: Test return List expenses: @Get /expenses, @RequestBody Account account")
    void expensesControllerTest() {
        Mockito.when(transactionService.expenses(mockAccount)).thenReturn(transactions);
        ResponseEntity<List<Transaction>> controllerResponse = transactionController.expenses(mockAccount);
        assertEquals(controllerResponse.getBody(), transactions);
    }

    @Test
    @DisplayName("CP07-05: Test return see-status (include expenses, incomes) /see-status ,  @RequestBody Account account")
    void seeStatusControllerTest() {
        Account account = mockAccount;
        Mockito.when(transactionService.seeStatus(mockAccount)).thenReturn(account);
        ResponseEntity<Account> controllerResponse = transactionController.seeStatus(mockAccount);
        assertEquals(controllerResponse.getBody(), mockAccount);
    }
}

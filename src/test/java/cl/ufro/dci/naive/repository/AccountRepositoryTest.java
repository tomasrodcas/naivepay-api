package cl.ufro.dci.naive.repository;

import cl.ufro.dci.naive.domain.Account;
import cl.ufro.dci.naive.domain.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static cl.ufro.dci.naive.repository.util.UtilTest.DATA_JSON_ACCOUNTS;
import static cl.ufro.dci.naive.repository.util.UtilTest.DATA_JSON_CUSTOMERS;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CustomerRepository customerRepository;

    @BeforeEach
    void initEach() throws IOException {
        Account[] accounts = new ObjectMapper().readValue(DATA_JSON_ACCOUNTS,Account[].class);
        Customer[] customers = new ObjectMapper().readValue(DATA_JSON_CUSTOMERS,Customer[].class);
        accounts[0].setAccCustomer(customers[0]); // 324512 -- Antonio Farias
        accounts[1].setAccCustomer(customers[1]); // 51223 -- Henry Topen
        accounts[2].setAccCustomer(customers[0]); //  324 -- Antonio Farias
        Arrays.stream(accounts).forEach(accountRepository::save);
        Arrays.stream(customers).forEach(customerRepository::save);
    }
    @AfterEach
    public  void cleanUp(){
        accountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    @Order(3)
    @DisplayName("-CP1- Test account saved successfully")
    void testAccountSavedSuccesfully(){
        Account newAccount = new Account();
        newAccount.setAccNum(12332);

        Account savedAccount = accountRepository.save(newAccount);

        Assertions.assertNotNull(savedAccount,"Account should be saved");
        Assertions.assertNotNull(savedAccount.getAccId(),"Account should have an id when saved");
        Assertions.assertEquals(newAccount.getAccNum(),savedAccount.getAccNum());
    }

    @Test
    @Order(3)
    @DisplayName("-CP2- Test Account find by Account number")
    void testFindAccountByAccNum() {
        Account newAccount = new Account();
        newAccount.setAccNum(234631);
        newAccount.setAccCvv(231);

        accountRepository.save(newAccount);

        Optional<Account> findAccount = accountRepository.findByAccNum(234631);

        Assertions.assertTrue(findAccount.isPresent(), "Access should be found");
        Assertions.assertEquals(newAccount.getAccCvv(),findAccount.get().getAccCvv());
    }

    @Test
    @Order(1)
    @DisplayName("-CP3- Test account deleted succesfully")
    void testAccountDeleted(){
        int count = (int) accountRepository.count();

        accountRepository.deleteById(1L);

        Assertions.assertEquals((count-1),accountRepository.count());
    }

    @Test
    @Order(4)
    @DisplayName("-CP4- Test account not found with non-existing id")
    void testAccountNotFoundNonExistingId(){
        Optional<Account> retrivedAccount = accountRepository.findById(Long.valueOf(100));

        Assertions.assertTrue(retrivedAccount.isEmpty(),"Account with id 100 should not exist");
    }

    @Test
    @Order(2)
    @DisplayName(" -CP5-Test account update succesfully")
    void testAccountUpdate(){
        Optional<Account> account = accountRepository.findById(4L);
        account.get().setAccAmount(9000);

        Account updateAccount = accountRepository.save(account.get());

        Assertions.assertEquals(9000,updateAccount.getAccAmount());
    }

    @Test
    @Order(6)
    @DisplayName("-CP6- Test Accounts find by Customer")
    void testFindAccountsByCustomer(){
        Optional<Customer> customer = customerRepository.findCustomerByCusName("Antonio Farias");

        List<Account> accounts = accountRepository.findAccountsByAccCustomer(customer.get());

        Assertions.assertEquals(2,accounts.size());
    }
}
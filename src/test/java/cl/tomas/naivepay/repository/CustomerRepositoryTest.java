package cl.tomas.naivepay.repository;

import cl.tomas.naivepay.infrastructure.models.Access;
import cl.tomas.naivepay.infrastructure.models.Customer;
import cl.tomas.naivepay.infrastructure.repository.AccessRepository;
import cl.tomas.naivepay.infrastructure.repository.CustomerRepository;
import cl.tomas.naivepay.repository.util.UtilTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;

import java.util.Arrays;
import java.util.Optional;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AccessRepository accessRepository;

    @BeforeEach
    void initEach() throws IOException {
        Customer[] customers = new ObjectMapper().readValue(UtilTest.DATA_JSON_CUSTOMERS,Customer[].class);
        Access[] accesses = new ObjectMapper().readValue(UtilTest.DATA_JSON_ACCESS,Access[].class);
        customers[0].setCusAccess(accesses[0]); //Antonio Farias -- username1
        customers[1].setCusAccess(accesses[1]); // Henry Topen -- UserName2
        Arrays.stream(accesses).forEach(accessRepository::save);
        Arrays.stream(customers).forEach(customerRepository::save);
    }
    @AfterEach
    public  void cleanUp(){
        customerRepository.deleteAll();
        accessRepository.deleteAll();}

    @Test
    @DisplayName("-CP1- Test customer saved successfully")
    void testCustomerSavedSuccesfully(){
        Customer newCustomer = new Customer();
        newCustomer.setCusName("john cena");

        Customer savedCustomer = customerRepository.save(newCustomer);

        Assertions.assertNotNull(savedCustomer,"Customer should be saved");
        Assertions.assertNotNull(savedCustomer.getCusId(),"Customer should have an id when saved");
        Assertions.assertEquals(newCustomer.getCusName(),savedCustomer.getCusName());
    }

    @Test
    @DisplayName("-CP2- Test customer find by Access id")
    void testCustomerFindByCusUsername() {
        Optional<Access> access = accessRepository.findByAccName("username1");

        Optional<Customer> Customer = customerRepository.findBycusAccess_accId(access.get().getAccId());

        Assertions.assertTrue(Customer.isPresent(), "Customer should be found");
        Assertions.assertEquals("Antonio Farias",Customer.get().getCusName());

    }

    @Test
    @DisplayName("-CP3- Test customer not found with non-existing id")
    void testCustomerNotFoundNonExistingId(){
        Optional<Customer> retrivedCustomer = customerRepository.findById(Long.valueOf(100));

        Assertions.assertTrue(retrivedCustomer.isEmpty(),"Customer with id 100 should not exist");
    }

    @Test
    @DisplayName("-CP4- Test customer deleted succesfully")
    void testCustomerDeleted(){
        int count = (int) customerRepository.count();

        customerRepository.deleteById(1L);

        Assertions.assertEquals((count-1),(int) customerRepository.count());
    }
}
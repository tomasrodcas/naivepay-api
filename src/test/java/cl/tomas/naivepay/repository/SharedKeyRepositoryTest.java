package cl.tomas.naivepay.repository;

import cl.tomas.naivepay.infrastructure.models.Customer;
import cl.tomas.naivepay.infrastructure.models.KeyState;
import cl.tomas.naivepay.infrastructure.models.SharedKey;
import cl.tomas.naivepay.infrastructure.repository.CustomerRepository;
import cl.tomas.naivepay.infrastructure.repository.KeyStateRepository;
import cl.tomas.naivepay.infrastructure.repository.SharedKeyRepository;
import cl.tomas.naivepay.repository.util.UtilTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SharedKeyRepositoryTest {

    @Autowired
    SharedKeyRepository sharedKeyRepository;
    @Autowired
    KeyStateRepository keyStateRepository;
    @Autowired
    CustomerRepository customerRepository;

    @BeforeEach
    void initEach() throws IOException {
        KeyState[] keyStates = new ObjectMapper().readValue(UtilTest.DATA_JSON_KEYSTATES, KeyState[].class);
        Customer[] customers = new ObjectMapper().readValue(UtilTest.DATA_JSON_CUSTOMERS, Customer[].class);
        String key1 = "llave1 laksjdalskdja";
        String key2 = "asdasdallave2 laksjdalskdja";
        SharedKey sharedKey1 = new SharedKey();
        sharedKey1.setShaKey(key1.getBytes(StandardCharsets.UTF_8));
        SharedKey sharedKey2 = new SharedKey();
        sharedKey2.setShaKey(key2.getBytes(StandardCharsets.UTF_8));
        SharedKey[] sharedKeys = {sharedKey1, sharedKey2};
        sharedKeys[0].setShaCustomer(customers[0]); // key1 -- Antonio Farias
        sharedKeys[1].setShaCustomer(customers[0]); // key2 -- Antonio Farias
        sharedKeys[0].setShaKeyState(keyStates[0]); // key1 -- Activada
        sharedKeys[1].setShaKeyState(keyStates[0]); // key2 -- Activada
        Arrays.stream(keyStates).forEach(keyStateRepository::save);
        Arrays.stream(customers).forEach(customerRepository::save);
        Arrays.stream(sharedKeys).forEach(sharedKeyRepository::save);
    }

    @AfterEach
    public void cleanUp() {
        sharedKeyRepository.deleteAll();
        keyStateRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    @Order(2)
    @DisplayName("-CP1- Test SharedKey saved successfully")
    void testSharedKeySavedSuccesfully() {
        String key = "testKEYKSJADHKJAHD";
        SharedKey newSharedKey = new SharedKey();
        newSharedKey.setShaKey(key.getBytes(StandardCharsets.UTF_8));

        SharedKey savedSharedKey = sharedKeyRepository.save(newSharedKey);

        Assertions.assertNotNull(savedSharedKey, "SharedKey should be saved");
        Assertions.assertNotNull(savedSharedKey.getShaId(), "SharedKey should have an id when saved");
        Assertions.assertEquals(newSharedKey.getShaKey(), savedSharedKey.getShaKey());
    }

    @Test
    @Order(1)
    @DisplayName("-CP2- Test SharedKey deleted succesfully")
    void testSharedKeyDeleted() {
        int count = (int) sharedKeyRepository.count();

        sharedKeyRepository.deleteById(1L);

        Assertions.assertEquals((count - 1), sharedKeyRepository.count());
    }

    @Test
    @Order(3)
    @DisplayName("-CP4- Test SharedKey not found with non-existing id")
    void testSharedKeyNotFoundNonExistingId() {
        Optional<SharedKey> retrivedSharedKey = sharedKeyRepository.findById(Long.valueOf(100));

        Assertions.assertTrue(retrivedSharedKey.isEmpty(), "SharedKey with id 100 should not exist");
    }

    @Test
    @Order(4)
    @DisplayName("-CP3- Test SharedKeys find by Customer")
    void testSharedKeysFindByCustomer() {
        Optional<Customer> customer = customerRepository.findCustomerByCusName("Antonio Farias");

        List<SharedKey> sharedKeys = sharedKeyRepository.findSharedKeyByShaCustomer(customer.get());

        Assertions.assertEquals(2, sharedKeys.size());
    }


}
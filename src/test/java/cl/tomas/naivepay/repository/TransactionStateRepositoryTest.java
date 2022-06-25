package cl.tomas.naivepay.repository;

import cl.tomas.naivepay.domain.TransactionState;
import cl.tomas.naivepay.repository.util.UtilTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@DataJpaTest
class TransactionStateRepositoryTest {

    @Autowired
    TransactionStateRepository transactionStateRepository;

    @BeforeEach
    void initEach() throws IOException {
        TransactionState[] transactionStates = new ObjectMapper().readValue(UtilTest.DATA_JSON_TRANSACTIONSTATES,TransactionState[].class);

        Arrays.stream(transactionStates).forEach(transactionStateRepository::save);
    }

    @AfterEach
    void cleanUp(){
        transactionStateRepository.deleteAll();
    }

    @Test
    @DisplayName("-CP1- Test TransactionState saved successfully")
    void testTransactionStateSavedSuccesfully(){
        TransactionState newState = new TransactionState();
        newState.setTrsName("testState");

        TransactionState savedState = transactionStateRepository.save(newState);

        Assertions.assertNotNull(savedState ,"TransactionState should be saved");
        Assertions.assertNotNull(savedState .getTrsId(),"TransactionState should have an id when saved");
        Assertions.assertEquals(newState.getTrsName(),savedState.getTrsName());
    }

    @Test
    @DisplayName("-CP2- Test TransactionState not found with non-existing id")
    void testTransactionStateNotFoundNonExistingId(){
        Optional<TransactionState> retrivedTransactionState = transactionStateRepository.findById(Long.valueOf(100));

        Assertions.assertTrue(retrivedTransactionState .isEmpty(),"TransactionState with id 100 should not exist");
    }

    @Test
    @DisplayName("-CP3- Test TransactionState find by state name")
    void testFindTransactionStateByAccNum() {
        TransactionState transactionState = new TransactionState();
        transactionState.setTrsName("testState");

        transactionStateRepository.save(transactionState);

        Optional<TransactionState> findTransactionState = transactionStateRepository.findByTrsName("testState");

        Assertions.assertTrue(findTransactionState.isPresent(), "TransactionState should be found");
        Assertions.assertEquals(transactionState.getTrsName(),findTransactionState.get().getTrsName());
    }

}
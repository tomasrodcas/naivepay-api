package cl.tomas.naivepay.repository;

import cl.tomas.naivepay.domain.Access;
import cl.tomas.naivepay.repository.util.UtilTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccessRepositoryTest {

    @Autowired
    AccessRepository accessRepository;

    @BeforeEach
    void initEach() throws IOException {
        Access[] accesses = new ObjectMapper().readValue(UtilTest.DATA_JSON_ACCESS,Access[].class);
        Arrays.stream(accesses).forEach(accessRepository::save);
    }
    @AfterEach
    public  void cleanUp(){
        accessRepository.deleteAll();
    }

    @Test
    @Order(2)
    @DisplayName("-CP1- Test Access saved successfully")
    void testAccessSavedSuccesfully(){
        Access newAccess = new Access();
        newAccess.setAccName("usernametest");

        Access savedAccess = accessRepository.save(newAccess);

        Assertions.assertNotNull(savedAccess,"Access should be saved");
        Assertions.assertNotNull(savedAccess.getAccId(),"Access should have an id when saved");
        Assertions.assertEquals(newAccess.getAccName(),savedAccess.getAccName());
    }

    @Test
    @Order(3)
    @DisplayName(" -CP4- Test Access find by name")
    void testFindAccessByName() {
        Access newAccess = new Access();
        newAccess.setAccName("Hola123");
        newAccess.setAccPassword("password");

        accessRepository.save(newAccess);

        Optional<Access> findAccess = accessRepository.findByAccName("Hola123");

        Assertions.assertTrue(findAccess.isPresent(), "Access should be found");
        Assertions.assertEquals(newAccess.getAccPassword(),findAccess.get().getAccPassword());
    }

    @Test
    @Order(4)
    @DisplayName("-CP3- Test Access not found with non-existing id")
    void testAccessNotFoundNonExistingId(){
        Optional<Access> retrivedAccess = accessRepository.findById(Long.valueOf(100));

        Assertions.assertTrue(retrivedAccess.isEmpty(),"Access with id 100 should not exist");
    }

    @Test
    @Order(1)
    @DisplayName("-CP2-Test Access deleted succesfully")
    void testAccessDeleted(){
        int count = (int) accessRepository.count();

        accessRepository.deleteById(1L);

        Assertions.assertEquals((count-1),(int) accessRepository.count());
    }

}
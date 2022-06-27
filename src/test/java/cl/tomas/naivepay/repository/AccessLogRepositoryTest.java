package cl.tomas.naivepay.repository;

import cl.tomas.naivepay.infrastructure.models.Access;
import cl.tomas.naivepay.infrastructure.models.AccessLog;
import cl.tomas.naivepay.infrastructure.repository.AccessLogRepository;
import cl.tomas.naivepay.infrastructure.repository.AccessRepository;
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
class AccessLogRepositoryTest {

    @Autowired
    AccessLogRepository accessLogRepository;
    @Autowired
    AccessRepository accessRepository;

    @BeforeEach
    void initEach() throws IOException {
        AccessLog[] accessLogs = new ObjectMapper().readValue(UtilTest.DATA_JSON_ACCESSLOGS,AccessLog[].class);
        Access[] accesses = new ObjectMapper().readValue(UtilTest.DATA_JSON_ACCESS,Access[].class);
        accessLogs[0].setAloAccess(accesses[0]);
        accessLogs[1].setAloAccess(accesses[0]);
        accessLogs[2].setAloAccess(accesses[0]);
        Arrays.stream(accessLogs).forEach(accessLogRepository::save);
        Arrays.stream(accesses).forEach(accessRepository::save);
    }

    @AfterEach
    public  void cleanUp(){
        accessLogRepository.deleteAll();
        accessRepository.deleteAll();
    }

    @Test
    @Order(2)
    @DisplayName("-CP1- Test AccessLog saved successfully")
    void testAccessLogSavedSuccesfully(){
        AccessLog newAccessLog = new AccessLog();
        newAccessLog.setAloDescription("Description acccesslog test");

        AccessLog savedAccessLog = accessLogRepository.save(newAccessLog);

        Assertions.assertNotNull(savedAccessLog,"AccessLog should be saved");
        Assertions.assertNotNull(savedAccessLog.getAloId(),"AccessLog should have an id when saved");
        Assertions.assertEquals(newAccessLog.getAloDescription(),savedAccessLog.getAloDescription());
    }

    @Test
    @Order(1)
    @DisplayName("-CP2- Test AccessLog deleted succesfully")
    void testAccessLogDeleted(){
        int count = (int) accessLogRepository.count();

        accessLogRepository.deleteById(1L);

        Assertions.assertEquals((count-1),(int) accessLogRepository.count());
    }

    @Test
    @Order(3)
    @DisplayName("-CP3- Test AccessLogs find by Access")
    void testAccessLogsFindByAccess(){
        Optional<Access> access = accessRepository.findByAccName("username1");

        List<AccessLog> accessLogs = accessLogRepository.findAccessLogsByAloAccess(access.get());

        Assertions.assertEquals(3,accessLogs.size());
    }




}
package cl.ufro.dci.naive.repository;

import cl.ufro.dci.naive.domain.KeyState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static cl.ufro.dci.naive.repository.util.UtilTest.DATA_JSON_KEYSTATES;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class KeyStateRepositoryTest {

    @Autowired
    KeyStateRepository keyStateRepository;

    @BeforeEach
    void initEach() throws IOException {
        KeyState[] keyStates = new ObjectMapper().readValue(DATA_JSON_KEYSTATES,KeyState[].class);

        Arrays.stream(keyStates).forEach(keyStateRepository::save);
    }

    @AfterEach
    void cleanUp(){
        keyStateRepository.deleteAll();
    }

    @Test
    @DisplayName("-CP1- Test KeyState saved successfully")
    void testKeyStateSavedSuccesfully(){
        KeyState newKeyState = new KeyState();
        newKeyState.setKstState("StateTest");

        KeyState savedKeyState = keyStateRepository.save(newKeyState);

        Assertions.assertNotNull(savedKeyState,"KeyState should be saved");
        Assertions.assertNotNull(savedKeyState.getKstId(),"KeyState should have an id when saved");
        Assertions.assertEquals(newKeyState.getKstState(),savedKeyState.getKstState());
    }

    @Test
    @DisplayName("-CP2- Test KeyState not found with non-existing id")
    void testKeyStateNotFoundNonExistingId(){
        Optional<KeyState> retrivedKeyState = keyStateRepository.findById(Long.valueOf(100));

        Assertions.assertTrue(retrivedKeyState.isEmpty(),"KeyState with id 100 should not exist");
    }

    @Test
    @DisplayName("-CP3- Test KeyState find by state name")
    void testFindKeyStateByAccNum() {
        KeyState keyState = new KeyState();
        keyState.setKstState("testState");

        keyStateRepository.save(keyState);

        Optional<KeyState> findKeyState = keyStateRepository.findByKstState("testState");

        Assertions.assertTrue(findKeyState.isPresent(), "KeyState should be found");
        Assertions.assertEquals(keyState.getKstState(),findKeyState.get().getKstState());
    }

}
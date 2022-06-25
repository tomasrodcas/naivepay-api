package cl.tomas.naivepay.repository;

import cl.tomas.naivepay.domain.KeyState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeyStateRepository extends CrudRepository<KeyState,Long> {
    Optional<KeyState> findByKstState(String kstState);
}

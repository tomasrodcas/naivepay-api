package cl.tomas.naivepay.infrastructure.repository;

import cl.tomas.naivepay.infrastructure.models.KeyState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeyStateRepository extends CrudRepository<KeyState,Long> {
    Optional<KeyState> findByKstState(String kstState);
}

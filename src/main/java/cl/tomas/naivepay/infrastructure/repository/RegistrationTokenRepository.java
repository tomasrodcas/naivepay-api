package cl.tomas.naivepay.infrastructure.repository;

import cl.tomas.naivepay.infrastructure.models.RegistrationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RegistrationTokenRepository extends CrudRepository<RegistrationToken, Long> {
    Optional<RegistrationToken> findRegistrationTokenByTkToken(String tkToken);
}

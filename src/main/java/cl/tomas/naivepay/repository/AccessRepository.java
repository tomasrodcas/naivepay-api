package cl.tomas.naivepay.repository;

import cl.tomas.naivepay.domain.Access;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessRepository extends CrudRepository<Access,Long> {

    Optional<Access> findByAccName(String accName);

}

package cl.ufro.dci.naive.repository;

import cl.ufro.dci.naive.domain.Access;
import cl.ufro.dci.naive.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessRepository extends CrudRepository<Access,Long> {

    Optional<Access> findByAccName(String accName);

}

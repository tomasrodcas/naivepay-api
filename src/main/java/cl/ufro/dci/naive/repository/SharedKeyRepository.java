package cl.ufro.dci.naive.repository;

import cl.ufro.dci.naive.domain.Customer;
import cl.ufro.dci.naive.domain.SharedKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SharedKeyRepository extends CrudRepository<SharedKey,Long> {
    List<SharedKey> findSharedKeyByShaCustomer(Customer customer);
}

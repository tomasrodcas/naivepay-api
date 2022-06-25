package cl.tomas.naivepay.repository;

import cl.tomas.naivepay.domain.Customer;
import cl.tomas.naivepay.domain.SharedKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SharedKeyRepository extends CrudRepository<SharedKey,Long> {
    List<SharedKey> findSharedKeyByShaCustomer(Customer customer);
}

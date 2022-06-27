package cl.tomas.naivepay.infrastructure.repository;

import cl.tomas.naivepay.infrastructure.models.Customer;
import cl.tomas.naivepay.infrastructure.models.SharedKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SharedKeyRepository extends CrudRepository<SharedKey,Long> {
    List<SharedKey> findSharedKeyByShaCustomer(Customer customer);
}

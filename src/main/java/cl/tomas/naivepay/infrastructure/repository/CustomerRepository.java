package cl.tomas.naivepay.infrastructure.repository;

import cl.tomas.naivepay.infrastructure.models.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByCusName(String cusName);
    Optional<Customer> findCustomerByCusName(String name);
    Optional<Customer> findBycusAccess_accId(long accId);
}

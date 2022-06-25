package cl.ufro.dci.naive.repository;

import cl.ufro.dci.naive.domain.Access;
import cl.ufro.dci.naive.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByCusName(String cusName);
    Optional<Customer> findCustomerByCusName(String name);
    Optional<Customer> findBycusAccess_accId(long accId);
}

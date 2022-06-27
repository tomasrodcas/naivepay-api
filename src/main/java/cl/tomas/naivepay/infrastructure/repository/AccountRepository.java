package cl.tomas.naivepay.infrastructure.repository;

import cl.tomas.naivepay.infrastructure.models.Account;
import cl.tomas.naivepay.infrastructure.models.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface AccountRepository extends CrudRepository<Account,Long> {
    Optional<Account> findByAccNum(long accNum);
    List<Account> findAccountsByAccCustomer(Customer customer);
}

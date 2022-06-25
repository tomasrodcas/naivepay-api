package cl.tomas.naivepay.repository;

import cl.tomas.naivepay.domain.Account;
import cl.tomas.naivepay.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface AccountRepository extends CrudRepository<Account,Long> {
    Optional<Account> findByAccNum(int accNum);
    List<Account> findAccountsByAccCustomer(Customer customer);
}

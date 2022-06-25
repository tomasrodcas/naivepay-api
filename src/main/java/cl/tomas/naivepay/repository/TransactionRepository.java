package cl.tomas.naivepay.repository;

import cl.tomas.naivepay.domain.Account;
import cl.tomas.naivepay.domain.Transaction;
import cl.tomas.naivepay.domain.TransactionState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Long>{

    Optional<List<Transaction>> findAllByTraOriginAccount(String traOriginAccount);

    Optional<List<Transaction>> findAllByTraDestinationAccount(String traDestinationAccount);

    List<Transaction> findTransactionsByTraAccount(Account account);
    List<Transaction> findTransactionsByTraTransactionState(TransactionState transactionState);
}

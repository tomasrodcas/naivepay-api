package cl.tomas.naivepay.infrastructure.repository;

import cl.tomas.naivepay.infrastructure.models.Account;
import cl.tomas.naivepay.infrastructure.models.Transaction;
import cl.tomas.naivepay.infrastructure.models.TransactionState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Long>{


    List<Transaction> findAllByTraDestinationAccount(Account traDestinationAccount);
    List<Transaction> findTransactionsByTraAccount(Account account);
    List<Transaction> findTransactionsByTraTransactionState(TransactionState transactionState);

    List<Transaction> findFirst10ByTraAccountOrTraDestinationAccountOrderByTraDate(Account account, Account destinationAcc);
}

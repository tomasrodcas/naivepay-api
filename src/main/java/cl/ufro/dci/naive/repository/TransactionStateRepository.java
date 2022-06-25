package cl.ufro.dci.naive.repository;

import cl.ufro.dci.naive.domain.TransactionState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionStateRepository extends CrudRepository<TransactionState,Long> {
    Optional<TransactionState> findByTrsName(String stateName);
}

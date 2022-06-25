package cl.ufro.dci.naive.service.transaction;

import cl.ufro.dci.naive.domain.Account;
import cl.ufro.dci.naive.domain.Transaction;
import cl.ufro.dci.naive.domain.TransactionState;
import cl.ufro.dci.naive.exceptions.ApiRequestException;
import cl.ufro.dci.naive.repository.AccountRepository;
import cl.ufro.dci.naive.repository.TransactionRepository;
import cl.ufro.dci.naive.repository.TransactionStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    Logger logger = LoggerFactory.getLogger(TransactionService.class);
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionStateRepository transactionStateRepository;

    public void setAccountRepository(AccountRepository newAccountRepository) {
        accountRepository = newAccountRepository;
    }

    public void setTransactionRepository(TransactionRepository newTransactionRepository) {
        transactionRepository = newTransactionRepository;
    }

    public void setTransactionStateRepository(TransactionStateRepository newTransactionStateRepository) {
        transactionStateRepository = newTransactionStateRepository;
    }

    public Account increaseAmount(Account account) {
        if (account.getAccId() != null) {
            var acc = accountRepository.findById(account.getAccId()).
                    orElseThrow(() -> new ApiRequestException("Account not exist"));
            acc.setAccAmount(acc.getAccAmount() + 10000);

            var transaction = new Transaction();
            transaction.setTraAmount(10000);
            transaction.setTraDescription("Increase amount");
            transaction.setTraDestinationAccount(String.valueOf(account.getAccId()));
            transactionRepository.save(transaction);
            return accountRepository.save(acc);
        } else {
            throw new ApiRequestException("Invalid data");
        }
    }

    public List<Transaction> transactionsPendingApproval(Account account) {
        if (account.getAccId() != null) {
            return transactionRepository.findAllByTraOriginAccount(String.valueOf(account.getAccId()))
                    .orElseThrow(() -> new ApiRequestException("Account not exist"))
                    .stream().filter(t -> t.getTraTransactionState().getTrsName().equals("Unpaid")).collect(Collectors.toList());
        } else {
            throw new ApiRequestException("Invalid data");
        }
    }

    public Transaction transferAmount( Transaction transaction) {
        TransactionState transactionState = transactionStateRepository.findById(transaction.getTraTransactionState().getTrsId())
                .orElseThrow(() -> new ApiRequestException("Id of transaction state not found"));
        if (transactionState.getTrsName().equals("Paid")) {
            throw new ApiRequestException("Transaction has already been completed");
        }
        if ((transaction.getTraOriginAccount() == null) || (transaction.getTraDestinationAccount() == null) || (transactionState.getTrsName() == null)) {
            throw new ApiRequestException("Insufficient data");
        }
        if (transaction.getTraOriginAccount().equals(transaction.getTraDestinationAccount())) {
            throw new ApiRequestException("Accounts must be different");
        }
        Account accOrigin = accountRepository.findById(Long.valueOf(transaction.getTraOriginAccount())).
                orElseThrow(() -> new ApiRequestException("Origin account not exist: " + transaction.getTraOriginAccount()));
        Account accDestination = accountRepository.findById(Long.valueOf(transaction.getTraDestinationAccount())).
                orElseThrow(() -> new ApiRequestException("Destination account not exist: " + transaction.getTraDestinationAccount()));
        if (transaction.getTraAmount() > 0) {
            if (accOrigin.getAccAmount() < transaction.getTraAmount()) {
                throw new ApiRequestException("Amount of origin account is insufficient");
            } else {
                var tra = new Transaction();
                tra.setTraAmount(transaction.getTraAmount());
                tra.setTraDescription(transaction.getTraDescription());
                tra.setTraOriginAccount(transaction.getTraOriginAccount());
                tra.setTraDestinationAccount(transaction.getTraDestinationAccount());

                transactionState.setTrsName("Paid");

                tra.setTraTransactionState(transactionState);

                accOrigin.setAccAmount(accOrigin.getAccAmount() - transaction.getTraAmount());
                accDestination.setAccAmount(accDestination.getAccAmount() + transaction.getTraAmount());

                accountRepository.save(accOrigin);
                accountRepository.save(accDestination);
                transactionRepository.save(transaction);
                return transaction;
            }
        } else {
            throw new ApiRequestException("Amount not valid");
        }
    }

    public Account seeStatus(Account account) {
        if (account.getAccId() != null) {
            return accountRepository.findById(account.getAccId()).
                    orElseThrow(() -> new ApiRequestException("Account not exist"));
        } else {
            throw new ApiRequestException("Invalid data");
        }
    }

    public List<Transaction> incomes(Account account) {
        if (account.getAccId() != null) {
            return transactionRepository.findAllByTraDestinationAccount(String.valueOf(account.getAccId())).
                    orElseThrow(() -> new ApiRequestException("Account not exist"));
        } else {
            throw new ApiRequestException("Invalid data");
        }
    }


    public List<Transaction> expenses(Account account){
        if (account.getAccId() != null) {
            return transactionRepository.findAllByTraOriginAccount(String.valueOf(account.getAccId())).
                    orElseThrow(() -> new ApiRequestException("Account not exist"));
        } else {
            throw new ApiRequestException("Invalid data");
        }
    }
}
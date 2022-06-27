package cl.tomas.naivepay.service.transaction;


import cl.tomas.naivepay.domain.entities.Deposit;
import cl.tomas.naivepay.domain.entities.TransactionEntity;
import cl.tomas.naivepay.domain.exceptions.ApiRequestException;
import cl.tomas.naivepay.infrastructure.repository.AccountRepository;
import cl.tomas.naivepay.infrastructure.repository.TransactionRepository;
import cl.tomas.naivepay.infrastructure.repository.TransactionStateRepository;
import cl.tomas.naivepay.infrastructure.models.Account;
import cl.tomas.naivepay.infrastructure.models.Transaction;
import cl.tomas.naivepay.infrastructure.models.TransactionState;
import cl.tomas.naivepay.service.account.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionStateRepository transactionStateRepository;

    public TransactionEntity deposit(Deposit deposit) {
            log.info("Depositing {} to account {}",deposit.getAmount() ,deposit.getAccId());
            if(deposit.getAmount() <= 0){
                throw new ApiRequestException("Can't deposit 0 or less money to the account");
            }
            Account account = accountService.getAccountById(deposit.getAccId());
            account.setAccAmount(account.getAccAmount()+ deposit.getAmount());

            try{

                account = accountService.updateAccount(account.toEntity());
                Transaction transaction = new Transaction();
                transaction.setTraAccount(account);
                transaction.setTraDestinationAccount(account);
                transaction.setTraAmount(deposit.getAmount());
                transaction.setTraDescription("Deposit");
                return transactionRepository.save(transaction).toEntity();

            } catch(Exception e){
                log.error("Error Making Deposit | {}", e.getMessage());
                throw new ApiRequestException("Error Making Deposit");
            }
    }

    public TransactionEntity makeTransaction(TransactionEntity transactionEntity) {
        this.validateTransactionData(transactionEntity);

        Account originAcc = accountService.getByAccNum(transactionEntity.getTraOriginAccountNum());
        Account destinationAcc = accountService.getByAccNum(transactionEntity.getTraDestinationAccountNum());

        try{
            Transaction transaction = new Transaction();
            transaction.setTraAmount(transactionEntity.getTraAmount());
            transaction.setTraDescription(transactionEntity.getTraDescription());
            transaction.setTraDestinationAccount(destinationAcc);
            transaction.setTraAccount(originAcc);
            transaction.setTraDate(new Date(System.currentTimeMillis()));

            TransactionState state = new TransactionState();
            state.setTrsName(transactionEntity.getTraState());

            transaction.setTraTransactionState(state);

            originAcc.setAccAmount(originAcc.getAccAmount() - transaction.getTraAmount());
            destinationAcc.setAccAmount(destinationAcc.getAccAmount() + transaction.getTraAmount());

            accountService.updateAccount(originAcc.toEntity());
            accountService.updateAccount(destinationAcc.toEntity());
            return transactionRepository.save(transaction).toEntity();

        }catch(Exception e){
            log.error("Error Making Transaction! | {}", e.getMessage());
            throw new ApiRequestException("Error Making Transaction!");
        }

    }

    public List<TransactionEntity> getRecentTransactions(long accId){
        Account account = accountService.getAccountById(accId);
        try{
            List<Transaction> transactions = transactionRepository.findFirst10ByTraAccountOrderByTraDate(account);
            return transactions.stream().map(Transaction::toEntity).collect(Collectors.toList());
        }catch(Exception e){
            log.error("Error Fetching Recent Transactions for Acc {}", accId);
            throw new ApiRequestException("Error Fetching Recent Transactions");
        }
    }

    public List<TransactionEntity> getByAccount(long accId){
        Account account = accountService.getAccountById(accId);
        try{
            List<Transaction> transactions = (List<Transaction>) transactionRepository.findTransactionsByTraAccount(account);
            return transactions.stream().map(Transaction::toEntity).collect(Collectors.toList());
        }catch(Exception e){
            log.error("Error Fetching Transactions | {}", e.getMessage());
            throw new ApiRequestException("Error Fetching Transactions");
        }
    }

    public List<TransactionEntity> incomes(long accId) {
        try{
            Account account = accountService.getAccountById(accId);
            List<Transaction> transactions = transactionRepository.findAllByTraDestinationAccount(account);
            return transactions.stream().map(Transaction::toEntity).collect(Collectors.toList());
        }catch(Exception e){
            log.error("Error Fetching Expenses for Account {} | {}", accId, e.getMessage());
            throw new ApiRequestException("Error Fetching Expenses");
        }

    }


    public List<TransactionEntity> expenses(long accId){
        try{
            Account account = accountService.getAccountById(accId);
            List<Transaction> transactions = transactionRepository.findTransactionsByTraAccount(account);
            return transactions.stream().map(Transaction::toEntity).collect(Collectors.toList());
        }catch(Exception e){
            log.error("Error Fetching Expenses for Account {} | {}", accId, e.getMessage());
            throw new ApiRequestException("Error Fetching Expenses");
        }

    }

    private void validateTransactionData(TransactionEntity transactionEntity){
        if(transactionEntity.getTraAmount() <= 0){
            throw new ApiRequestException("Can't Make a Transaction With 0 or less amount");
        }

        Account originAcc = accountService.getByAccNum(transactionEntity.getTraOriginAccountNum());
        Account destinationAcc = accountService.getByAccNum(transactionEntity.getTraDestinationAccountNum());
        if(Objects.equals(originAcc.getAccId(), destinationAcc.getAccId())){
            throw new ApiRequestException("Origin and Destination Accounts Can't be the Same!");
        }
        if(originAcc.getAccAmount() < transactionEntity.getTraAmount()){
            throw new ApiRequestException("Insufficient Funds!");
        }

    }
}
package cl.tomas.naivepay.service.account;

import cl.tomas.naivepay.domain.entities.AccountEntity;
import cl.tomas.naivepay.domain.exceptions.ApiForbiddenException;
import cl.tomas.naivepay.domain.exceptions.ApiRequestException;
import cl.tomas.naivepay.infrastructure.repository.AccountRepository;
import cl.tomas.naivepay.infrastructure.repository.CustomerRepository;
import cl.tomas.naivepay.service.customer.CustomerService;
import cl.tomas.naivepay.infrastructure.models.Account;
import cl.tomas.naivepay.infrastructure.models.Customer;
import cl.tomas.naivepay.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountService {

    @Autowired
    AccountRepository repository;

    @Autowired
    AuthService authService;

    @Autowired
    CustomerRepository customerRepository;

    public Account getAccountById(long id) {
        log.info("Finding Account with ID " + id);
        try {
            Account account = repository.findById(id).orElseThrow();
            authService.checkResourceOwnership(account.getAccCustomer().getCusId(),
                    "Can't access Account Without Ownership or Permissions");
            return account;

        } catch (ApiForbiddenException e) {
            log.error("Error Fetching Account - No Ownership");
            throw new ApiForbiddenException(e.getMessage());
        } catch (NoSuchElementException e) {
            log.error("Account with ID" + id + " Not Found  " + e.getMessage());
            throw new ApiRequestException("No Account with ID: " + id + " Found");
        } catch (Exception e) {
            log.error("Error searching for account with ID " + id + " " + e.getMessage());
            e.printStackTrace();
            throw new ApiRequestException("Error Searching Account with ID: " + id);
        }
    }

    public Account createAccount(AccountEntity accountEntity) {
        log.info("Creating Account");
        try {
            Customer accountOwner = customerRepository.findById(accountEntity
                    .getAccCustomer().getCusId()).orElseThrow();
            Account account = new Account();
            account.setAccCustomer(accountOwner);
            account.setAccAmount(0);
            account.setAccCvv(generateRandomCVV());
            account.setAccNum(generateRandomPAN());
            return repository.save(account);
        } catch (Exception e) {
            log.error("Error Creating Account!: " + e);
            throw new ApiRequestException("Error Creating Account");
        }

    }

    public Account updateAccount(AccountEntity accountEntity) {
        String errorMessage = "Error Updating Account with ID: " + accountEntity.getAccId();
        log.info("Updating Account");
        try {
            Account accountStored = repository.findById(accountEntity.getAccId()).orElseThrow();
            authService.checkResourceOwnership(accountStored.getAccCustomer().getCusId(),
                        "Can't Update Account without Ownership or Permissions");
            buildFromEntity(accountStored, accountEntity);
            return repository.save(accountStored);
        } catch (ApiForbiddenException e) {
            log.error("Error Fetching Account - No Ownership");
            throw new ApiForbiddenException(e.getMessage());
        } catch (NoSuchElementException e) {
            log.error(errorMessage + " Account Not Found");
            throw new ApiRequestException("Account with ID: " + accountEntity.getAccId() + " Not Found");
        } catch (Exception e) {
            log.error(errorMessage + " " + e.getMessage());
            throw new ApiRequestException(errorMessage);
        }

    }

    public List<Account> getAccounts() {
        log.info("Finding all Accounts");
        try {
            return (List<Account>) repository.findAll();
        } catch (Exception e) {
            log.error("Error Searching for accounts");
            throw new ApiRequestException("Error Fetching Accounts!");
        }

    }

    public boolean deleteAccount(long id) {
        log.warn("Deleting Account With ID: " + id);
        try {
            Account account = repository.findById(id).orElseThrow();
            repository.delete(account);
            return true;
        } catch (NoSuchElementException e) {
            log.error("No Account With ID: " + id + " Found");
            throw new ApiRequestException("No Account With ID: " + id + " Found For Deletion");
        } catch (Exception e) {
            log.error("Error Deleting Account With ID: " + id);
            throw new ApiRequestException("Error Deleting Account With ID: " + id);
        }
    }

    public List<Account> getByCustomer(long cusId){
        log.info("Fetching Customer {} Accounts", cusId);
        try{
            Customer customer = customerRepository.findById(cusId).orElseThrow();
            authService.checkResourceOwnership(cusId,
                    "Can't Access Account without Ownership or Permissions");
            return repository.findAccountsByAccCustomer(customer);

        }catch (ApiForbiddenException e) {
            log.error("Error Fetching Account - No Ownership");
            throw new ApiForbiddenException(e.getMessage());
        }catch(Exception e ){
            log.error("Error Fetching Customer {} Accounts", cusId);
            throw new ApiRequestException("Error fetching accounts for customer"+cusId);
        }
    }

    public Account getByAccNum(long accNum){
        log.info("Finding Account with num " + accNum);
        try {
            return repository.findByAccNum(accNum).orElseThrow();
        }catch (NoSuchElementException e) {
            log.error("Account with num" + accNum + " Not Found  " + e.getMessage());
            throw new ApiRequestException("No Matching Account Found");
        } catch (Exception e) {
            log.error("Error searching for account with num"+ accNum+" "+ e.getMessage());
            e.printStackTrace();
            throw new ApiRequestException("Error searching for account with num"+ accNum);
        }
    }

    public int getAccountCvv(long accId){
        log.info("Finding Account Cvv " + accId);
        try {
            Account account = repository.findById(accId).orElseThrow();
            return account.getAccCvv();
        }catch (NoSuchElementException e) {
            log.error("Account with ID" + accId + " Not Found  " + e.getMessage());
            throw new ApiRequestException("No Matching Account Found");
        } catch (Exception e) {
            log.error("Error searching for cvv | {}", e.getMessage());
            e.printStackTrace();
            throw new ApiRequestException("Error searching for cvv");
        }
    }

    private void buildFromEntity(Account account, AccountEntity entity){
        account.setAccId(entity.getAccId());
        account.setAccAmount(entity.getAccAmount());
        account.setAccCvv(entity.getAccCvv());
        account.setAccCustomer(customerRepository.findById(entity.getAccCustomer().getCusId()).orElseThrow());
    }

    private long generateRandomPAN() {
        Random random = new Random();
        char[] digits = new char[16];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < 16; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }

    private int generateRandomCVV(){
        Random random = new Random();
        char[] digits = new char[3];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < 3; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Integer.parseInt(new String(digits));
    }
}
package cl.ufro.dci.naive.service.account;

import cl.ufro.dci.naive.domain.Account;
import cl.ufro.dci.naive.domain.Customer;
import cl.ufro.dci.naive.exceptions.ApiForbiddenException;
import cl.ufro.dci.naive.exceptions.ApiRequestException;
import cl.ufro.dci.naive.repository.AccountRepository;
import cl.ufro.dci.naive.service.auth.AuthService;
import cl.ufro.dci.naive.service.customer.CustomerService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class AccountService {

    @Autowired
    AccountRepository repository;

    @Autowired
    AuthService authService;

    @Autowired
    CustomerService customerService;

    public Account getAccountById(long id) {
        log.info("Finding Account with ID " + id);
        try {
            Account account = repository.findById(id).orElseThrow();
            authService.checkResourceOwnership(account.getAccCustomer().getCusId());
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

    public Account createAccount(Account account) {
        log.info("Creating Account");
        try {
            return repository.save(account);
        } catch (Exception e) {
            log.error("Error Creating Account!: " + e);
            throw new ApiRequestException("Error Creating Account");
        }

    }

    public Account updateAccount(Account account) {
        String errorMessage = "Error Updating Account with ID: " + account.getAccId();
        log.info("Updating Account");
        try {
            Account accountStored = repository.findById(account.getAccId()).orElseThrow();
            authService.checkResourceOwnership(accountStored.getAccCustomer().getCusId());
            return repository.save(account);
        } catch (ApiForbiddenException e) {
            log.error("Error Fetching Account - No Ownership");
            throw new ApiForbiddenException(e.getMessage());
        } catch (NoSuchElementException e) {
            log.error(errorMessage + " Account Not Found");
            throw new ApiRequestException("Account with ID: " + account.getAccId() + " Not Found");
        } catch (Exception e) {
            log.error(errorMessage + " " + e.getMessage());
            throw new ApiRequestException(errorMessage);
        }

    }

    public List<Account> getAccounts() {
        log.info("Finding all Accounts");
        try {
            return (List<Account>) repository.findAll();
        } catch (NullPointerException e) {
            log.error("No Account Found");
            throw new ApiRequestException("No Accounts Found!");
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
            Customer customer = customerService.getCustomerById(cusId);
            authService.checkResourceOwnership(cusId);
            return repository.findAccountsByAccCustomer(customer);
        }catch (ApiForbiddenException e) {
            log.error("Error Fetching Account - No Ownership");
            throw new ApiForbiddenException(e.getMessage());
        }catch(Exception e ){
            log.error("Error Fetching Customer {} Accounts", cusId);
            throw new ApiRequestException("Error fetching accounts for customer"+cusId);
        }
    }
}
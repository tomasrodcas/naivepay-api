package cl.ufro.dci.naive.service.customer;

import cl.ufro.dci.naive.domain.Customer;
import cl.ufro.dci.naive.exceptions.ApiForbiddenException;
import cl.ufro.dci.naive.exceptions.ApiRequestException;
import cl.ufro.dci.naive.repository.CustomerRepository;

import cl.ufro.dci.naive.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    AuthService authService;
    @Autowired
    CustomerRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository) {
        this.repository = customerRepository;
    }

    public List<Customer> getCustomers() {
        log.info("Fetching all customers");
        try {
            return (List<Customer>) repository.findAll();
        } catch (Exception e) {
            log.error("Error fetching customers {}", e.toString());
            throw new ApiRequestException("Error fetching customers!");
        }
    }

    public Customer getCustomerById(long id) {
        log.info("Fetching Customer With ID: {}", id);
        try {
            authService.checkResourceOwnership(id);
            return repository.findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            log.error("No Customer With ID: " + id + " Found!");
            throw new ApiRequestException("No Customer With ID: " + id + " Found!");
        } catch (ApiForbiddenException e) {
            log.error(e.getMessage());
            throw new ApiForbiddenException(e.getMessage());
        } catch (Exception e) {
            log.error("Error Fetching Customer {}", e.toString());
            e.printStackTrace();
            throw new ApiRequestException("Error Fetching Customer");
        }
    }

    public Customer createCustomer(Customer customer) {
        log.info("Creating New Customer!");
        try {
            return repository.save(customer);
        } catch (Exception e) {
            log.error("Error Creating New Customer {}", e.toString());
            throw new ApiRequestException("Error Creating New Customer!");
        }

    }

    public Customer updateCustomer(Customer customer) {
        log.info("Updating Customer With ID: {}", customer.getCusId());
        try {
            Customer customerStored = repository.findById(customer.getCusId()).orElseThrow();
            authService.checkResourceOwnership(customerStored.getCusId());
            return repository.save(customer);
        } catch (NoSuchElementException e) {
            log.error("No Customer With ID: " + customer.getCusId() + " Found!");
            throw new ApiRequestException("No Customer With ID: " + customer.getCusId() + " Found!");
        } catch (ApiForbiddenException e) {
            log.error(e.getMessage());
            throw new ApiForbiddenException(e.getMessage());
        } catch (Exception e) {
            log.error("Error Updating Customer With ID: " + customer.getCusId() + " " + e.toString());
            throw new ApiRequestException("Error Updating Customer!");
        }
    }

    public boolean deleteCustomer(long id) {
        log.warn("Deleting Customer With ID: {}", id);
        try {
            Customer customer = repository.findById(id).orElseThrow();
            repository.delete(customer);
            return true;
        } catch (NoSuchElementException e) {
            log.error("No Customer With ID: " + id + " Found!");
            throw new ApiRequestException("No Customer With ID: " + id + " Found!");
        } catch (Exception e) {
            log.error("Error Deleting Customer With ID: " + id + " " + e.toString());
            throw new ApiRequestException("Error Deleting Customer!");
        }
    }

}
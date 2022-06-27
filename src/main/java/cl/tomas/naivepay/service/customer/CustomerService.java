package cl.tomas.naivepay.service.customer;

import cl.tomas.naivepay.domain.entities.CustomerEntity;
import cl.tomas.naivepay.domain.exceptions.ApiForbiddenException;
import cl.tomas.naivepay.domain.exceptions.ApiRequestException;
import cl.tomas.naivepay.infrastructure.models.Access;
import cl.tomas.naivepay.infrastructure.repository.CustomerRepository;
import cl.tomas.naivepay.infrastructure.models.Customer;

import cl.tomas.naivepay.service.access.AccessService;
import cl.tomas.naivepay.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    AccessService accessService;


    public List<Customer> getCustomers() {
        log.info("Fetching all customers");
        try {
            return (List<Customer>) repository.findAll();
        } catch (Exception e) {
            log.error("Error fetching customers {}", e.toString());
            throw new ApiRequestException("Error fetching customers!");
        }
    }

    public Customer getById(long id) {
        log.info("Fetching Customer With ID: {}", id);
        try {
            authService.checkResourceOwnership(id,
                    "Can't Access Customer Info without Ownership or Permissions");
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

    public Customer createCustomer(CustomerEntity customerEntity) {
        log.info("Creating New Customer!");
        try {
            Customer customer = new Customer();
            Access access = accessService.create(customerEntity.getCusAccess());
            customer.setCusAccess(access);
            buildFromEntity(customer, customerEntity);
            return repository.save(customer);
        } catch (Exception e) {
            log.error("Error Creating New Customer {}", e.toString());
            throw new ApiRequestException("Error Creating New Customer!");
        }

    }

    public CustomerEntity updateCustomer(CustomerEntity customerEntity) {
        log.info("Updating Customer With ID: {}", customerEntity.getCusId());
        try {
            Customer customerStored = repository.findById(customerEntity.getCusId()).orElseThrow();
            authService.checkResourceOwnership(customerStored.getCusId(),
                    "Can't Update Customer Info without Ownership or Permissions");
            buildFromEntity(customerStored, customerEntity);

            return repository.save(customerStored).toEntity();

        } catch (NoSuchElementException e) {
            log.error("No Customer With ID: " + customerEntity.getCusId() + " Found!");
            throw new ApiRequestException("No Customer With ID: " + customerEntity.getCusId() + " Found!");
        } catch (ApiForbiddenException e) {
            log.error(e.getMessage());
            throw new ApiForbiddenException(e.getMessage());
        } catch (Exception e) {
            log.error("Error Updating Customer With ID: " + customerEntity.getCusId() + " " + e.toString());
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

    private void buildFromEntity(Customer customer, CustomerEntity customerEntity){
        customer.setCusName(customerEntity.getCusName());
        customer.setCusAddress(customerEntity.getCusAddress());
        customer.setCusMail(customerEntity.getCusMail());
        customer.setCusBirthDate(customerEntity.getCusBirthDate());
        customer.setCusRut(customerEntity.getCusRut());
        customer.setCusDevice(customerEntity.getCusDevice());
        customer.setCusDv(customerEntity.getCusDv());
    }

}
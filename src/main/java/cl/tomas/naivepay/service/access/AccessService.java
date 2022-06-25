package cl.tomas.naivepay.service.access;

import java.util.Date;
import java.util.NoSuchElementException;

import cl.tomas.naivepay.exceptions.ApiRequestException;
import cl.tomas.naivepay.repository.AccessRepository;
import cl.tomas.naivepay.repository.CustomerRepository;
import cl.tomas.naivepay.service.accesslog.AccessLogService;
import cl.tomas.naivepay.domain.AccessLog;
import cl.tomas.naivepay.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cl.tomas.naivepay.domain.Access;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccessService implements UserDetailsService {

    @Autowired
    AccessRepository repository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccessLogService accessLogService;

    public Access getWithPassword(long id) {
        try {
            return repository.findById(id).orElseThrow();
        } catch (Exception e) {
            log.error("Error Fetching Access ID: ", id);
            throw new ApiRequestException("Error Fetching Access ID: " + id);
        }
    }

    public Access getWithoutPassword(long id) {
        try {
            Access access = repository.findById(id).orElseThrow();
            access.setAccPassword("");
            return access;
        } catch (Exception e) {
            log.error("Error Fetching Access ID: ", id);
            throw new ApiRequestException("Error Fetching Access ID: " + id);
        }
    }

    public Access updatePassword(long id, String password) {
        try {
            Access access = repository.findById(id).orElseThrow();
            access.setAccPassword(passwordEncoder.encode(password));
            return repository.save(access);
        } catch (Exception e) {
            log.error("Error Updating Access Password ID: ", id);
            throw new ApiRequestException("Error Updating Access Password ID: " + id);
        }
    }

    public Access create(Access access) {
        try {
            log.info("Creating Access! ", access.getAccName());
            access.setAccPassword(passwordEncoder.encode(access.getAccPassword()));
            return repository.save(access);
        } catch (Exception e) {
            log.error("Error Creating Access");
            throw new ApiRequestException("Error Creating Access");
        }
    }

    public Boolean deleteById(long id) {
        try {
            Access access = repository.findById(id).orElseThrow();
            repository.delete(access);
            return true;
        } catch (Exception e) {
            log.error("Error Deleting Access ID: {} Throwable: {}", id, e.getCause());
            throw new ApiRequestException("Error Deleting Access ID: " + id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Fetching Customer With Username: " + username);
        try {
            Access access = repository.findByAccName(username).orElseThrow();
            AccessLog accessLog = new AccessLog();
            accessLog.setAloAccess(access);
            accessLog.setAloDate(new Date());
            accessLog.setAloDescription("Log In");
            accessLogService.create(accessLog);
            return access;

        } catch (NoSuchElementException e) {
            log.error("No Customer With Username {} Found", username);
            throw new ApiRequestException("No Customer With Username " + username + " Found");
        } catch (Exception e) {
            log.error("Error Fetching Customer With Username " + username + " " + e.toString());
            throw new ApiRequestException("Error Fetching Customer");
        }
    }

    public Customer getCustomerByAccessId(long accId) {
        log.info("Fetching Customer With Access ID: " + accId);
        try {
            return customerRepository.findBycusAccess_accId(accId).orElseThrow();
        } catch (NoSuchElementException e) {
            log.error("No Customer");
            throw new ApiRequestException("No Customer");
        } catch (Exception e) {
            log.error("Error Updating Customer With I " + e.toString());
            throw new ApiRequestException("Error Updating Customer!");
        }
    }

}

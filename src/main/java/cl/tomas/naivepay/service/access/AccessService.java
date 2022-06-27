package cl.tomas.naivepay.service.access;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.NoSuchElementException;

import cl.tomas.naivepay.domain.entities.AccessEntity;
import cl.tomas.naivepay.domain.entities.CustomerEntity;
import cl.tomas.naivepay.domain.exceptions.ApiRequestException;
import cl.tomas.naivepay.infrastructure.repository.AccessRepository;
import cl.tomas.naivepay.infrastructure.repository.CustomerRepository;
import cl.tomas.naivepay.service.accesslog.AccessLogService;
import cl.tomas.naivepay.infrastructure.models.AccessLog;
import cl.tomas.naivepay.infrastructure.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cl.tomas.naivepay.infrastructure.models.Access;
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

    public AccessEntity getWithoutPassword(long id) {
        try {
            return repository.findById(id).orElseThrow().toEntity();

        } catch (Exception e) {
            log.error("Error Fetching Access ID: ", id);
            throw new ApiRequestException("Error Fetching Access ID: " + id);
        }
    }

    public AccessEntity updatePassword(long id,String oldPassword, String password) {

        try {
            Access access = repository.findById(id).orElseThrow();
            if(!passwordEncoder.encode(oldPassword).equals(access.getPassword())){
                throw new ApiRequestException("Old Password is Incorrect");
            }
            access.setAccPassword(passwordEncoder.encode(password));
            return repository.save(access).toEntity();
        } catch (Exception e) {
            log.error("Error Updating Access Password ID: ", id);
            throw new ApiRequestException("Error Updating Access Password ID: " + id);
        }
    }

    public Access create(AccessEntity accessEntity) {
        try {
            log.info("Creating Access! ", accessEntity.getAccName());
            Access access = new Access();
            buildFromEntity(access, accessEntity);
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
            return repository.findByAccName(username).orElseThrow();

        } catch (NoSuchElementException e) {
            log.error("No Customer With Username {} Found", username);
            throw new ApiRequestException("No Customer With Username " + username + " Found");
        } catch (Exception e) {
            log.error("Error Fetching Customer With Username " + username + " " + e.toString());
            throw new ApiRequestException("Error Fetching Customer");
        }
    }

    public CustomerEntity getCustomerByAccessId(long accId) {
        log.info("Fetching Customer With Access ID: " + accId);
        try {
            return customerRepository.findBycusAccess_accId(accId).orElseThrow().toEntity();
        } catch (NoSuchElementException e) {
            log.error("No Customer");
            throw new ApiRequestException("No Customer");
        } catch (Exception e) {
            log.error("Error Updating Customer With I " + e.toString());
            throw new ApiRequestException("Error Updating Customer!");
        }
    }

    public AccessEntity update(AccessEntity accessEntity){
        log.warn("Updating Access");
        try{
            Access access = repository.findById(accessEntity.getAccId()).orElseThrow();
            buildFromEntity(access,accessEntity);
            return repository.save(access).toEntity();
        }catch(Exception e){
            log.error("Error Updating Access | {}",e.getMessage());
            throw new ApiRequestException("Error Updating Access");
        }
    }

    public AccessEntity block(long accId){
        log.warn("Blocking Access");
        try{
            Access access = repository.findById(accId).orElseThrow();
            access.setBlocked(true);
            return repository.save(access).toEntity();
        }catch(Exception e){
            log.error("Error Updating Access | {}",e.getMessage());
            throw new ApiRequestException("Error Updating Access");
        }
    }
    public AccessEntity unblock(long accId){
        log.warn("Unblocking Access");
        try{
            Access access = repository.findById(accId).orElseThrow();
            access.setBlocked(false);
            return repository.save(access).toEntity();
        }catch(Exception e){
            log.error("Error Updating Access | {}",e.getMessage());
            throw new ApiRequestException("Error Updating Access");
        }
    }


    private void buildFromEntity(Access access, AccessEntity accessEntity){
        if(accessEntity.getAccPassword() != null){
            access.setAccPassword(passwordEncoder.encode(accessEntity.getAccPassword()));
        }
        access.setAccRole(accessEntity.getAccRole());
        access.setAccName(accessEntity.getAccName());
        access.setEnabled(accessEntity.isEnabled());
    }

}

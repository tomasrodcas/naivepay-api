package cl.tomas.naivepay.service.registration;

import cl.tomas.naivepay.domain.entities.CustomerEntity;
import cl.tomas.naivepay.domain.exceptions.ApiRequestException;
import cl.tomas.naivepay.infrastructure.models.Access;
import cl.tomas.naivepay.infrastructure.models.Customer;
import cl.tomas.naivepay.infrastructure.models.RegistrationToken;
import cl.tomas.naivepay.service.access.AccessService;
import cl.tomas.naivepay.service.customer.CustomerService;
import cl.tomas.naivepay.service.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegistrationService {

    @Autowired
    CustomerService customerService;

    @Autowired
    RegistrationTokenService tokenService;

    @Autowired
    AccessService accessService;

    @Autowired
    EmailService emailService;

    public boolean register(CustomerEntity customerEntity){
        log.info("Registering");
        try{
            Customer customer = customerService.createCustomer(customerEntity);
            RegistrationToken token = tokenService.generate(customer.getCusAccess());
            emailService.sendRegistrationEmail(customer, token.getTkToken());
            return true;

        }catch(Exception e){
            log.error("Error registering! | {}", e.getMessage());
            throw new ApiRequestException("Error registering");
        }
    }

    public boolean confirmEmail(String tk){
        log.info("Validating Email");
        RegistrationToken token = tokenService.confirm(tk);
        try{
            Access access = token.getTkAccess();
            access.setEnabled(true);
            accessService.update(access.toEntity());
            return true;

        }catch(Exception e){
            log.error("Error Confirming Email! | {}", e.getMessage());
            throw new ApiRequestException("Error Confirming Email!");
        }
    }
}

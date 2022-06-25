package cl.ufro.dci.naive.service.auth;

import cl.ufro.dci.naive.domain.Customer;
import cl.ufro.dci.naive.exceptions.ApiForbiddenException;
import cl.ufro.dci.naive.exceptions.ApiRequestException;
import cl.ufro.dci.naive.repository.CustomerRepository;
import cl.ufro.dci.naive.security.authentication.AuthenticationFacade;
import cl.ufro.dci.naive.service.access.AccessService;
import cl.ufro.dci.naive.service.customer.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
public class AuthService {
    @Autowired
    AuthenticationFacade authentication;

    @Autowired
    AccessService accessService;

    public void checkResourceOwnership(long customerId) {
        if (!(accessService.getCustomerByAccessId(authentication.getAuthenticatedAccessId()).getCusId() == customerId
                || authentication.getAuthenticatedRole() == 1)) {
            throw new ApiForbiddenException("You don't have access to this resource");
        }
    }


}

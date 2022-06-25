package cl.tomas.naivepay.service.auth;

import cl.tomas.naivepay.exceptions.ApiForbiddenException;
import cl.tomas.naivepay.security.authentication.AuthenticationFacade;
import cl.tomas.naivepay.service.access.AccessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

package cl.tomas.naivepay.security.authentication;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();

    String getUsername();

    long getAuthenticatedAccessId();

    int getAuthenticatedRole();
}

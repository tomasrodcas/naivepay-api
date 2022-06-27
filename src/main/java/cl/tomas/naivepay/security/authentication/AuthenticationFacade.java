package cl.tomas.naivepay.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import cl.tomas.naivepay.infrastructure.models.Access;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getUsername() {
        Access access = (Access) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return access.getUsername();
    }

    @Override
    public long getAuthenticatedAccessId() {
        Access access = (Access) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return access.getAccId();
    }

    @Override
    public int getAuthenticatedRole() {
        Access access = (Access) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return access.getAccRole();
    }
}
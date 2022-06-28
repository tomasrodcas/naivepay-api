package cl.tomas.naivepay.service.auth;

import cl.tomas.naivepay.domain.entities.LoginResponse;
import cl.tomas.naivepay.domain.entities.CustomerEntity;
import cl.tomas.naivepay.domain.exceptions.ApiForbiddenException;
import cl.tomas.naivepay.domain.exceptions.ApiRequestException;
import cl.tomas.naivepay.infrastructure.models.Access;
import cl.tomas.naivepay.infrastructure.models.AccessLog;
import cl.tomas.naivepay.security.authentication.AuthenticationFacade;
import cl.tomas.naivepay.security.jwt.JwtDecoder;
import cl.tomas.naivepay.security.jwt.JwtEncoder;
import cl.tomas.naivepay.service.access.AccessService;
import cl.tomas.naivepay.service.accesslog.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
public class AuthService {
    @Autowired
    AuthenticationFacade authentication;

    @Autowired
    AccessService accessService;

    @Autowired
    AccessLogService accessLogService;

    @Value("${spring.jwt.secret}")
    private String secret;

    @Autowired
    AuthenticationManager authenticationManager;

    public void checkResourceOwnership(long customerId, String description) {
        Access access = accessService.getWithPassword(authentication.getAuthenticatedAccessId());
        if (!(accessService.getCustomerByAccessId(authentication.getAuthenticatedAccessId()).getCusId() == customerId
                || authentication.getAuthenticatedRole() == 1)) {
            accessLogService.create(createLog(access, description));
            throw new ApiForbiddenException("You don't have access to this resource");
        }
    }

    public LoginResponse login(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        Access access = (Access) authentication.getPrincipal();
        CustomerEntity customer = accessService.getCustomerByAccessId(access.getAccId());
        String issuer = request.getRequestURI();

        JwtEncoder jwtEncoder = new JwtEncoder();

        String token = jwtEncoder.encodeToken(access, customer, issuer, new Date(System.currentTimeMillis() + 10 * 60 * 1000),
                secret);
        String refreshToken = jwtEncoder.encodeRefreshToken(access, issuer,
                new Date(System.currentTimeMillis() + 30 * 60 * 1000), secret);

        accessLogService.create(createLog(access, "Logged In"));
        return new LoginResponse(customer.getCusId(),customer.getCusName() , access.getAccRole(), token, refreshToken);

    }

    public LoginResponse refreshToken(HttpServletRequest request){
        log.info("Refreshing Token");
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ApiRequestException("No Refresh Token Found");
        }
        try {
            String token = authorizationHeader.substring("Bearer ".length());
            Access decodedAccess = new JwtDecoder().decodeRefreshToken(token, secret);

            Access access = accessService.getWithPassword(decodedAccess.getAccId());
            CustomerEntity customer = accessService.getCustomerByAccessId(access.getAccId());
            JwtEncoder jwtEncoder = new JwtEncoder();
            String issuer = request.getRequestURL().toString();

            String newToken = jwtEncoder.encodeToken(access,customer, issuer,
                    new Date(System.currentTimeMillis() + 10 * 60 * 1000), secret);

            String newRefreshToken = jwtEncoder.encodeToken(access, customer, issuer,
                    new Date(System.currentTimeMillis() + 30 * 60 * 1000), secret);

            accessLogService.create(createLog(access, "Refreshed Token"));
            return new LoginResponse(customer.getCusId(),customer.getCusName() , access.getAccRole(), newToken, newRefreshToken);


        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage());
            throw new ApiRequestException("Authentication Error " + e.getCause());
        }
    }

    private AccessLog createLog(Access access, String description){
        AccessLog accessLog = new AccessLog();
        accessLog.setAloAccess(access);
        accessLog.setAloDate(LocalDateTime.now());
        accessLog.setAloDescription(description);
        return accessLog;
    }


}

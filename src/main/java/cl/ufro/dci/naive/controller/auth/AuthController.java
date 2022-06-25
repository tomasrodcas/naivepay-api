package cl.ufro.dci.naive.controller.auth;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.ufro.dci.naive.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cl.ufro.dci.naive.domain.Access;
import cl.ufro.dci.naive.exceptions.ApiRequestException;
import cl.ufro.dci.naive.security.jwt.JwtDecoder;
import cl.ufro.dci.naive.security.jwt.JwtEncoder;
import cl.ufro.dci.naive.service.access.AccessService;
import cl.ufro.dci.naive.service.customer.CustomerService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Value("${spring.jwt.secret}")
    private String secret;
    @Autowired
    AccessService accessService;

    @Autowired
    CustomerService customerService;
    @Autowired
    AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(HttpServletRequest request, HttpServletResponse response){
        log.error("ALOOOO");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        Access access = (Access) authentication.getPrincipal();
        Customer customer = accessService.getCustomerByAccessId(access.getAccId());
        String issuer = request.getRequestURI();

        JwtEncoder jwtEncoder = new JwtEncoder();

        String token = jwtEncoder.encodeToken(access, issuer, new Date(System.currentTimeMillis() + 10 * 60 * 1000),
                secret);
        String refreshToken = jwtEncoder.encodeRefreshToken(access, issuer,
                new Date(System.currentTimeMillis() + 30 * 60 * 1000), secret);

        response.setHeader("access_token", token);
        response.setHeader("refresh_token", refreshToken);
        LoginResponse responsePayload = new LoginResponse(customer.getCusId(),customer.getCusName() , access.getAccRole(), token, refreshToken);
        return ResponseEntity.status(200).body(responsePayload);
    }

    @GetMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("Refreshing Token");
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ApiRequestException("No Refresh Token Found");
        }
        try {
            String token = authorizationHeader.substring("Bearer ".length());
            Access decodedAccess = new JwtDecoder().decodeRefreshToken(token, secret);

            Access access = accessService.getWithPassword(decodedAccess.getAccId());
            JwtEncoder jwtEncoder = new JwtEncoder();
            String issuer = request.getRequestURL().toString();

            String newToken = jwtEncoder.encodeToken(access, issuer,
                    new Date(System.currentTimeMillis() + 10 * 60 * 1000), secret);

            String newRefreshToken = jwtEncoder.encodeToken(access, issuer,
                    new Date(System.currentTimeMillis() + 30 * 60 * 1000), secret);

            response.setHeader("access_token", newToken);
            response.setHeader("refresh_token", newRefreshToken);
        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage());
            response.setHeader("Error", e.getMessage());
            throw new ApiRequestException("Authentication Error " + e.getCause());
        }
    }
}

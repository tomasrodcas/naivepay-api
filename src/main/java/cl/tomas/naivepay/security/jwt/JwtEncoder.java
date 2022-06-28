package cl.tomas.naivepay.security.jwt;

import java.util.Date;
import java.util.stream.Collectors;

import cl.tomas.naivepay.domain.entities.CustomerEntity;
import org.springframework.security.core.GrantedAuthority;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import cl.tomas.naivepay.infrastructure.models.Access;

public class JwtEncoder {

    public String encodeToken(Access access, CustomerEntity customer, String issuer, Date expDate, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withSubject(access.getAccName())
                .withClaim("accId", access.getAccId())
                .withClaim("cusId", customer.getCusId())
                .withExpiresAt(expDate)
                .withIssuer(issuer)
                .withClaim("roles",
                        access.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String encodeRefreshToken(Access access, String issuer, Date expDate, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withSubject(access.getAccName())
                .withClaim("accId", access.getAccId())
                .withExpiresAt(expDate)
                .withIssuer(issuer)
                .sign(algorithm);
    }

}
package cl.ufro.dci.naive.security.jwt;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import cl.ufro.dci.naive.domain.Access;

public class JwtEncoder {

    public String encodeToken(Access access, String issuer, Date expDate, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withSubject(access.getAccName())
                .withClaim("access_id", access.getAccId())
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
                .withClaim("access_id", access.getAccId())
                .withExpiresAt(expDate)
                .withIssuer(issuer)
                .sign(algorithm);
    }

}
package cl.ufro.dci.naive.security.jwt;

import cl.ufro.dci.naive.domain.Access;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class JwtDecoderTest {

    private static Algorithm algorithm;
    private static String issuer;

    private static Access access;

    private static Date expDate;

    private static String secret;

    @BeforeAll
    static void setup(){
        secret = "test123";
        issuer = "test123";
        algorithm = Algorithm.HMAC256("test123".getBytes());
        access = new Access();
        access.setAccId(1L);
        access.setAccRole(1);
        access.setAccName("test123");
    }

    @BeforeEach
    void setupDate(){
        expDate = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
    }

    private String getToken(){
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

    @Test
    void decodeAccessToken() {
        String token = getToken();

        assertEquals(access.getAccId(), new JwtDecoder().decodeAccessToken(token, secret).getAccId());
        assertEquals(access.getAccName(), new JwtDecoder().decodeAccessToken(token, secret).getAccName());
        assertEquals(access.getAccRole(), new JwtDecoder().decodeAccessToken(token, secret).getAccRole());
    }

    @Test
    void decodeRefreshToken() {
        String token = getToken();

        assertEquals(access.getAccId(), new JwtDecoder().decodeRefreshToken(token, secret).getAccId());
        assertEquals(access.getAccName(), new JwtDecoder().decodeRefreshToken(token, secret).getAccName());
        assertEquals(0, new JwtDecoder().decodeRefreshToken(token, secret).getAccRole());
    }

    @Test
    void shouldThrowVerificationExceptionRefreshToken(){
        expDate = new Date(System.currentTimeMillis() - 10000*60);
        String token = getToken();
        assertThrows(JWTVerificationException.class, () -> {new JwtDecoder().decodeRefreshToken(token, secret);});

    }

    @Test
    void shouldThrowVerificationExceptionToken(){
        expDate = new Date(System.currentTimeMillis() - 10000*60);
        String token = getToken();
        assertThrows(JWTVerificationException.class, () -> {new JwtDecoder().decodeAccessToken(token, secret);});
    }
}
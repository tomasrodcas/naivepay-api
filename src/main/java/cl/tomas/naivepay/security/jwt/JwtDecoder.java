package cl.tomas.naivepay.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import cl.tomas.naivepay.infrastructure.models.Access;

import java.util.Date;

public class JwtDecoder {

    public Access decodeAccessToken(String token, String secret) throws JWTVerificationException {
        DecodedJWT decodedJWT = decode(token, secret);

        Access access = new Access();
        access.setAccId((long) decodedJWT.getClaim("accId").asInt());
        access.setAccName(decodedJWT.getSubject());
        access.setAccRole(Integer.parseInt(decodedJWT.getClaim("roles").asArray(String.class)[0]));

        return access;
    }

    public Access decodeRefreshToken(String token, String secret) throws JWTVerificationException {
        DecodedJWT decodedJWT = decode(token, secret);
        Access access = new Access();
        access.setAccId((long) decodedJWT.getClaim("accId").asInt());
        access.setAccName(decodedJWT.getSubject());

        return access;
    }

    private DecodedJWT decode(String token, String secret){
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public static boolean isExpired(String token){
        DecodedJWT jwt = JWT.decode(token);
        if( jwt.getExpiresAt().before(new Date())) {
            return true;
        }
        return false;
    }

}

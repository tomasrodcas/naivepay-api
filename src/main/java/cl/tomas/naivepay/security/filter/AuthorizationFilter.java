package cl.tomas.naivepay.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import cl.tomas.naivepay.infrastructure.models.Access;
import cl.tomas.naivepay.security.jwt.JwtDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {
    
    private String secret;

    public void setJwtSecret(String secret) {
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.equals("/auth/login") || path.equals("/auth/refresh-token")
                || path.startsWith("/customers/confirm-email/") || path.equals("/customers/register")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.error("Request Without Authorization");

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            PrintWriter bodyPrintWriter = response.getWriter();
            bodyPrintWriter.println("{ Message: No Authorization Token Found! }");
            bodyPrintWriter.flush();
            return;
        }
        try {
            String token = authorizationHeader.substring("Bearer ".length());
            Access decodedAccess = new JwtDecoder().decodeAccessToken(token, secret);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(Integer.toString(decodedAccess.getAccRole())));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    decodedAccess, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error Authenticating: {}", e.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            PrintWriter bodyPrintWriter = response.getWriter();
            bodyPrintWriter.println("{ Message: " + e.getMessage() + "}");
            bodyPrintWriter.flush();
        }

    }

}

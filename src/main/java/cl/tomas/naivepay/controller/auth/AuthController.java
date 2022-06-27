package cl.tomas.naivepay.controller.auth;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.tomas.naivepay.domain.entities.LoginResponse;
import cl.tomas.naivepay.infrastructure.models.AccessLog;
import cl.tomas.naivepay.domain.exceptions.ApiRequestException;
import cl.tomas.naivepay.service.accesslog.AccessLogService;
import cl.tomas.naivepay.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.tomas.naivepay.infrastructure.models.Access;
import cl.tomas.naivepay.security.jwt.JwtDecoder;
import cl.tomas.naivepay.security.jwt.JwtEncoder;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    AuthService authService;
    @Autowired
    AccessLogService accessLogService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.status(200).body(authService.login(request));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(200).body(authService.refreshToken(request));
    }

    @GetMapping("/get-all-access-logs")
    public ResponseEntity<List<AccessLog>> getLogs(){
        return ResponseEntity.status(200).body(accessLogService.getAll());
    }
}

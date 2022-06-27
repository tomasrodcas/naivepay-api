package cl.tomas.naivepay.service.registration;

import cl.tomas.naivepay.domain.exceptions.ApiRequestException;
import cl.tomas.naivepay.infrastructure.models.Access;
import cl.tomas.naivepay.infrastructure.models.RegistrationToken;
import cl.tomas.naivepay.infrastructure.repository.RegistrationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class RegistrationTokenService {

    @Autowired
    RegistrationTokenRepository repository;

    public RegistrationToken generate(Access access){
        try{
            RegistrationToken token = new RegistrationToken();
            String tk = UUID.randomUUID().toString();
            token.setTkToken(tk);
            token.setTkIssuedDate(LocalDateTime.now());
            token.setTkExpDate(LocalDateTime.now().plusMinutes(15));
            token.setTkAccess(access);
            return repository.save(token);
        }catch(Exception e){
            log.error("Error Generating Token | ", e.getMessage());
            throw new ApiRequestException("Error generating token");
        }
    }

    public RegistrationToken getByToken(String token){
        return repository.findRegistrationTokenByTkToken(token).orElseThrow();
    }

    public RegistrationToken confirm(String token){
        LocalDateTime datetime = LocalDateTime.now();
        RegistrationToken tokenStored = repository.findRegistrationTokenByTkToken(token).orElseThrow();
        if(tokenStored.getTkConfirmedDate() != null){
            throw new IllegalArgumentException("Token Already Confirmed!");
        }
        if(tokenStored.getTkExpDate().isBefore(datetime)){
            throw new IllegalArgumentException("Verification Token Expired!");
        }
        tokenStored.setTkConfirmedDate(datetime);
        return repository.save(tokenStored);
    }

}

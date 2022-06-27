package cl.tomas.naivepay.service.email;

import cl.tomas.naivepay.domain.exceptions.ApiRequestException;
import cl.tomas.naivepay.infrastructure.models.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService{

    @Value("{spring.mail.username}")
    String apiMailAddress;

    @Value("${spring.app.url}")
    String appUrl;

    @Autowired
    JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String body){

        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(apiMailAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        }catch(Exception e){
            log.error("Error Sending Email | {}", e.getMessage());
            throw new ApiRequestException("Error Sending Mail!");
        }
    }

    @Async
    public void sendRegistrationEmail(Customer customer, String token){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(apiMailAddress);
            message.setSubject("Verifica tu correo - NaivePay");
            message.setTo(customer.getCusMail());
            message.setText("Verifica tu direccion de correo "+appUrl+"/customers/confirm-email/"+token);
            mailSender.send(message);
        }catch(Exception e){
            log.error("Error Sending Registration Email | {}", e.getMessage());
            throw new ApiRequestException("Error Sending Registration Email");
        }

    }
}

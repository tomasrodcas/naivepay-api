package cl.tomas.naivepay.service.email;

import cl.tomas.naivepay.domain.exceptions.ApiRequestException;
import cl.tomas.naivepay.infrastructure.models.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

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
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(apiMailAddress);
            helper.setSubject("Verifica tu correo - NaivePay");
            helper.setTo(customer.getCusEmail());
            String url = String.format("%s/confirm-email/%s",appUrl,token);
            helper.setText(String.format("Verifica tu direccion de correo <a href='%s'> Verificar Correo </a>", url), true);
            mailSender.send(mimeMessage);
        }catch(Exception e){
            log.error("Error Sending Registration Email | {}", e.getMessage());
            e.printStackTrace();
            throw new ApiRequestException("Error Sending Registration Email");
        }

    }
}

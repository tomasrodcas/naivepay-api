package cl.tomas.naivepay.controller.customer;

import cl.tomas.naivepay.domain.entities.CustomerEntity;
import cl.tomas.naivepay.infrastructure.models.Customer;
import cl.tomas.naivepay.service.customer.CustomerService;

import cl.tomas.naivepay.service.email.EmailService;
import cl.tomas.naivepay.service.registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService service;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    EmailService emailService;

    @GetMapping("/get")
    public ResponseEntity<List<CustomerEntity>> getAllCustomers() {
        return ResponseEntity.status(200).body(service.getCustomers()
                .stream().map(Customer::toEntity)
                .collect(Collectors.toList()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CustomerEntity> getCustomerById(@PathVariable long id) {
        return ResponseEntity.status(200).body(service.getById(id).toEntity());
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerEntity> createCustomer(@RequestBody CustomerEntity customerEntity) {
        return ResponseEntity.status(200).body(service.createCustomer(customerEntity).toEntity());
    }

    @PutMapping("/update")
    public ResponseEntity<CustomerEntity> updateCustomer(@RequestBody CustomerEntity customerEntity) {
        return ResponseEntity.status(200).body(service.updateCustomer(customerEntity));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteCustomer(@PathVariable long id) {
        return ResponseEntity.status(200).body(service.deleteCustomer(id));
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> registerCustomer(@RequestBody CustomerEntity customerEntity){
        return ResponseEntity.status(200).body(registrationService.register(customerEntity));
    }

    @GetMapping("/confirm-email/{tk}")
    public ResponseEntity<Boolean> confirmEmail(@PathVariable String tk){
        return ResponseEntity.status(200).body(registrationService.confirmEmail(tk));
    }

    @GetMapping("/test-mail")
    public void testMail(){
        emailService.sendEmail("tomasrodcas@gmail.com", "probando", "ojala funcione esta wea");
    }
}

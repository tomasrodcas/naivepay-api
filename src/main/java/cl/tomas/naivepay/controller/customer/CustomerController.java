package cl.tomas.naivepay.controller.customer;

import cl.tomas.naivepay.domain.Customer;
import cl.tomas.naivepay.service.customer.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService service;

    @GetMapping("/get")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.status(200).body(service.getCustomers());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable long id) {
        return ResponseEntity.status(200).body(service.getCustomerById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.status(200).body(service.createCustomer(customer));
    }

    @PutMapping("/update")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) {
        return ResponseEntity.status(200).body(service.updateCustomer(customer));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteCustomer(@PathVariable long id) {
        return ResponseEntity.status(200).body(service.deleteCustomer(id));
    }
}

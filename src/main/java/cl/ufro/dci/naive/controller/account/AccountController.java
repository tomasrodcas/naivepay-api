package cl.ufro.dci.naive.controller.account;

import cl.ufro.dci.naive.domain.Account;
import cl.ufro.dci.naive.service.account.AccountService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService service;

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.status(200).body(service.getAccounts());
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getAccountById(@PathVariable long id) {
        return ResponseEntity.status(200).body(service.getAccountById(id));

    }

    @PutMapping("/update")
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
        return ResponseEntity.status(200).body(service.updateAccount(account));
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.status(200).body(service.createAccount(account));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteAccount(@PathVariable long id) {
        return ResponseEntity.status(200).body(service.deleteAccount(id));
    }

    @GetMapping("/get-by-customer/{id}")
    public ResponseEntity<List<Account>> getAccountsByCustomer(@PathVariable long id){
        return ResponseEntity.status(200).body(service.getByCustomer(id));
    }

}

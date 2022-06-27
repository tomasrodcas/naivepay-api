package cl.tomas.naivepay.controller.account;

import cl.tomas.naivepay.domain.entities.AccountEntity;
import cl.tomas.naivepay.infrastructure.models.Account;
import cl.tomas.naivepay.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService service;

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AccountEntity>> getAccounts() {
        return ResponseEntity.status(200).body(service.getAccounts()
                .stream().map(Account::toEntity)
                .collect(Collectors.toList()));
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountEntity> getAccountById(@PathVariable long id) {
        return ResponseEntity.status(200).body(service.getAccountById(id).toEntity());

    }

    @GetMapping("/get-by-num/{accNum}")
    public ResponseEntity<AccountEntity> getAccountByNum(@PathVariable int accNum){
        return ResponseEntity.status(200).body(service.getByAccNum(accNum).toEntity());
    }

    @PutMapping("/update")
    public ResponseEntity<AccountEntity> updateAccount(@RequestBody AccountEntity account) {
        return ResponseEntity.status(200).body(service.updateAccount(account).toEntity());
    }

    @PostMapping("/create")
    public ResponseEntity<AccountEntity> createAccount(@RequestBody AccountEntity account) {
        return ResponseEntity.status(200).body(service.createAccount(account).toEntity());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteAccount(@PathVariable long id) {
        return ResponseEntity.status(200).body(service.deleteAccount(id));
    }

    @GetMapping("/get-by-customer/{id}")
    public ResponseEntity<List<AccountEntity>> getAccountsByCustomer(@PathVariable long id){
        return ResponseEntity.status(200).body(service.getByCustomer(id).stream().map(Account::toEntity).
                collect(Collectors.toList()));
    }

}

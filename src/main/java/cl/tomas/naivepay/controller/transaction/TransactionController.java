package cl.tomas.naivepay.controller.transaction;

import cl.tomas.naivepay.domain.entities.Deposit;
import cl.tomas.naivepay.domain.entities.TransactionEntity;
import cl.tomas.naivepay.infrastructure.models.Account;
import cl.tomas.naivepay.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PutMapping("/deposit")
    public ResponseEntity<TransactionEntity> deposit(@RequestBody Deposit deposit) {
        return ResponseEntity.status(200).body(transactionService.deposit(deposit));
    }

    @PutMapping("/transfer")
    public ResponseEntity<TransactionEntity> transfer(@RequestBody TransactionEntity transaction) {
        return ResponseEntity.status(200).body(transactionService.makeTransaction(transaction));
    }

    @GetMapping("/incomes/{accId}")
    public ResponseEntity<List<TransactionEntity>> incomes(@PathVariable long accId) {
        return ResponseEntity.status(200).body(transactionService.incomes(accId));
    }

    @GetMapping("/expenses/{accId}")
    public ResponseEntity<List<TransactionEntity>> expenses(@PathVariable long accId) {
        return ResponseEntity.status(200).body(transactionService.expenses(accId));
    }

    @GetMapping("/get-by-account/{accId}")
    public ResponseEntity<List<TransactionEntity>> getByAccount(@PathVariable long accId){
        return ResponseEntity.status(200).body(transactionService.getByAccount(accId));
    }

    @GetMapping("/get-recent/{accNum}")
    public ResponseEntity<List<TransactionEntity>> getRecent(@PathVariable long accNum){
        return ResponseEntity.status(200).body(transactionService.getRecentTransactions(accNum));
    }
}
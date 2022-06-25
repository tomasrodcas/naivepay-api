package cl.ufro.dci.naive.controller.transaction;

import cl.ufro.dci.naive.domain.Account;
import cl.ufro.dci.naive.domain.Transaction;
import cl.ufro.dci.naive.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PutMapping("/increase-amount")
    public ResponseEntity<Account> increaseAmount(@RequestBody Account account) {
        return ResponseEntity.status(200).body(transactionService.increaseAmount(account));
    }

    @PutMapping("/transfer-amount")
    public ResponseEntity<Transaction> transferAmount(@RequestBody Transaction transaction) {
        return ResponseEntity.status(200).body(transactionService.transferAmount(transaction));
    }

    @GetMapping("/see-status")
    public ResponseEntity<Account> seeStatus(@RequestBody Account account) {
        return ResponseEntity.status(200).body(transactionService.seeStatus(account));
    }

    @GetMapping("/incomes")
    public ResponseEntity<List<Transaction>> incomes(@RequestBody Account account) {
        return ResponseEntity.status(200).body(transactionService.incomes(account));
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<Transaction>> expenses(@RequestBody Account account) {
        return ResponseEntity.status(200).body(transactionService.expenses(account));
    }
}
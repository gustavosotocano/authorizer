package com.authorizer.adapter.rest;

import com.authorizer.domain.model.Account;
import com.authorizer.domain.model.Transaction;
import com.authorizer.port.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/v1/transaction")
    public ResponseEntity<Account> transaction(@Valid @RequestBody Transaction transaction) {

        Account account = transactionService.addTransaction(transaction);
        if(!account.getViolations().isEmpty()){
            return new ResponseEntity<>(account, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }
}

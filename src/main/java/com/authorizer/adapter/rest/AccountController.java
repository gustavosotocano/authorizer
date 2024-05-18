package com.authorizer.adapter.rest;

import com.authorizer.domain.model.Account;
import com.authorizer.domain.model.AccountDetail;
import com.authorizer.port.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class AccountController {

    private final AccountService accountService;

    private static final int JUSTONEACCOUNT = 1;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/v1/account")
    public ResponseEntity<Account> addClient(@Valid @RequestBody AccountDetail accountDetail) {

        accountDetail.setAccountId(JUSTONEACCOUNT);//assumme it's only one account
        Account account=new Account(accountDetail);
        account = accountService.addAccount(account);
        if(!account.getViolations().isEmpty()){
            return new ResponseEntity<>(account, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }
}

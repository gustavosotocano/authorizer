package com.authorizer.domain.service;

import com.authorizer.domain.model.Account;
import com.authorizer.domain.model.Transaction;
import com.authorizer.adapter.persistence.AccountRepository;
import com.authorizer.port.AccountService;
import org.springframework.stereotype.Service;


/**
 * The class is an use case implementation of the inbound port.
 */
@Service
public class AccountServiceImplementation implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImplementation(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccountById(Integer accountId) {
        return accountRepository.getAccountById(accountId);
    }



    @Override
    public Account addAccount(Account account) {
        Account  acc=getAccountById(1);

        if(null!=acc){
            acc.getViolations().clear();
            acc.getViolations().add("account-already-initialized");
           return acc;
        }


        return accountRepository.addAccount(account);
    }
    @Override public Account updateAvailableLimit(Transaction transaction) {
        Account  account=getAccountById(1);
        if(null==account){
            account=new Account();
            account.getViolations().add("account-not-initialized");
        }else {
            account = accountRepository.updateAvailableLimit(transaction);
        }
        return account;
    }

    @Override public void remove() {
        accountRepository.remove();
    }

}

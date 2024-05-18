package com.authorizer.domain.service;

import com.authorizer.adapter.persistence.AccountRepository;
import com.authorizer.adapter.persistence.TransactionRepository;
import com.authorizer.domain.model.Account;
import com.authorizer.domain.model.Transaction;
import com.authorizer.domain.service.validacion.InsufficientLimit;
import com.authorizer.domain.service.validacion.LastThreeTransaction;
import com.authorizer.domain.service.validacion.SameLastTransaction;
import com.authorizer.domain.service.validacion.Validation;
import com.authorizer.port.TransactionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The class is an use case implementation of the inbound port.
 */
@Service
public class TransactionServiceImplementation implements TransactionService {

  private final TransactionRepository transactionRepository;

  private final AccountRepository accountRepository;

  private final LastThreeTransaction lastThreeTransaction;
  private final SameLastTransaction  sameLastTransaction;
  private final InsufficientLimit    insufficientLimit;

  public TransactionServiceImplementation(TransactionRepository transactionRepository, AccountRepository accountRepository, LastThreeTransaction lastThreeTransaction, SameLastTransaction sameLastTransaction, InsufficientLimit insufficientLimit) {
    this.transactionRepository = transactionRepository;
    this.accountRepository = accountRepository;
    this.lastThreeTransaction = lastThreeTransaction;
    this.sameLastTransaction = sameLastTransaction;
    this.insufficientLimit = insufficientLimit;
  }

  @Override
  public Account addTransaction(Transaction transaction) {

    Account account = accountRepository.getAccountById(1);
    if (null == account) {
      account = new Account();
      account.getViolations().add("account-not-initialized");
      return account;
    }
    account.getViolations().clear();
    if (!account.getAccount().isActiveCard()) {
      account.getViolations().add("card-not-active");
    }
    final List<Validation> conditions = new ArrayList<>();

    conditions.add(lastThreeTransaction);
    conditions.add(sameLastTransaction);
    conditions.add(insufficientLimit);

    for (final Validation condition : conditions) {
      final Optional<String> error = condition.validate(transaction, account);
      if (error.isPresent()) {
        account.getViolations().add(error.get());
      }
    }

    if (account.getViolations().size() > 0) {
      return account;
    }
    account = accountRepository.updateAvailableLimit(transaction);
    transactionRepository.addTransaction(transaction);
    return account;
  }

  @Override public List<Transaction> lastTransactions(Transaction transaction) {
    return transactionRepository.lastTransactions(transaction);
  }

  @Override public List<Transaction> sameLastTransactions(Transaction transaction) {
    return transactionRepository.sameLastTransactions(transaction);
  }

  @Override public void remove() {
  transactionRepository.remove();
  }

  ;

}

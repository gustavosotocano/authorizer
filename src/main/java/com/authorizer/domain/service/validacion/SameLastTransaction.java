package com.authorizer.domain.service.validacion;

import com.authorizer.adapter.persistence.TransactionRepository;
import com.authorizer.domain.model.Account;
import com.authorizer.domain.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SameLastTransaction implements Validation {

  private final TransactionRepository transactionRepository;

    public SameLastTransaction(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    public Optional<String> validate(final Transaction transaction, Account account) {

     if (transactionRepository.sameLastTransactions(transaction).isEmpty()) {
       return Optional.empty();

     }

    return Optional.of("doubled-transaction");
  }
}




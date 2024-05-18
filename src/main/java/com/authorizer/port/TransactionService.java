package com.authorizer.port;

import com.authorizer.domain.model.Account;
import com.authorizer.domain.model.Transaction;

import java.util.List;

/**
 * The interface is an inbound port provides the flow and the application functionality to the outside
 */
public interface TransactionService {

  Account addTransaction(Transaction transaction);
  List<Transaction> lastTransactions(Transaction transaction);
  List<Transaction> sameLastTransactions(Transaction transaction);
  void remove();
}

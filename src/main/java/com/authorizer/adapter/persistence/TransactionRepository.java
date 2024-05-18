package com.authorizer.adapter.persistence;

import com.authorizer.domain.model.Transaction;
import java.util.List;

/**
 * The repository interface is an outbound port that enables communication from the core application to a database
 * or other external data
 */
public interface TransactionRepository {

  Transaction addTransaction(Transaction transaction);

  List<Transaction> lastTransactions(Transaction transaction);

  List<Transaction> sameLastTransactions(Transaction transaction);

  void remove();
}

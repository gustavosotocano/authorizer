package com.authorizer.port;

import com.authorizer.domain.model.Transaction;
import com.authorizer.domain.model.Account;

/**
 * The interface is an inbound port provides the flow and the application functionality to the outside
 */
public interface AccountService {

   Account addAccount(Account account);

   Account getAccountById(Integer accountId);

   Account updateAvailableLimit(Transaction transaction);

   void remove();

}

package com.authorizer.adapter.socket;


import com.authorizer.domain.model.Account;
import com.authorizer.domain.model.AccountDetail;

import com.authorizer.adapter.persistence.AccountRepository;
import com.authorizer.port.AccountService;
import com.authorizer.adapter.persistence.TransactionRepository;
import com.authorizer.port.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@PropertySource("classpath:authorizer.properties")
public class AccountTest {
  @Autowired
  private AccountService        accountService;
  @Autowired
  private TransactionService    transactionService;
  @Mock
  private AccountRepository     accountRepository;
@Mock
private   TransactionRepository transactionRepository;

  @Test
  public void awhenAccountReturnNull() throws JsonProcessingException {
    String json= "{\"account\": {\": true, \"available-limit\": 100}}";
    Account account=new Account();
    AccountDetail accountDetail=new AccountDetail(1,true, 100);
    account.setAccount(accountDetail);
    //when(accountService.getAccountById(1)).willReturn(null);
    given(accountRepository.addAccount(account)).willReturn(account);
    AuthorizerController authorizerController =new AuthorizerController(accountService,transactionService);

   json= authorizerController.executeAuthorizer(json);


    String finalJson = json;
    Assertions.assertAll("test",
            () -> assertEquals("{\"account\":null,\"violations\":[\"inbound-message-does-not-have-the-allowed-structure\"]}"
                    , finalJson, "JSON no es el esperado")
         );
  }
  @Test
  public void bwhenAccountDontExist() throws JsonProcessingException {
    accountService.remove();
    String json= "{\"account\": {\"active-card\": true, \"available-limit\": 100}}";
    Account account=new Account();
    AccountDetail accountDetail=new AccountDetail(1,true, 100);
    account.setAccount(accountDetail);
    Mockito.when(accountRepository.getAccountById(1)).thenReturn(null);
   // Mockito.when(accountRepository.addAccount(account)).thenReturn(account);
   given(accountRepository.addAccount(account)).willReturn(account);
    AuthorizerController authorizerController =new AuthorizerController(accountService,transactionService);

    json= authorizerController.executeAuthorizer(json);

    String finalJson = json;
    Assertions.assertAll("test",
            () -> assertEquals("{\"account\":{\"active-card\":true,\"available-limit\":100},\"violations\":[]}"
                    , finalJson, "Account Exist")
    );
  }
  @Test
  public void cwhenAccountExist() throws JsonProcessingException {
    String json= "{\"account\": {\"active-card\": true, \"available-limit\": 100}}";
    Account account=new Account();
    AccountDetail accountDetail=new AccountDetail(1,true, 100);
    account.setAccount(accountDetail);

    Mockito.when(accountRepository.getAccountById(1)).thenReturn(account);
    given(accountRepository.addAccount(account)).willReturn(account);

    AuthorizerController authorizerController =new AuthorizerController(accountService,transactionService);

    json= authorizerController.executeAuthorizer(json);

    String finalJson = json;
    Assertions.assertAll("test",
            () -> assertEquals("{\"account\":{\"active-card\":true,\"available-limit\":100},\"violations\":[\"account-already-initialized\"]}"
                    , finalJson, "Account Exist")
    );

  }


}

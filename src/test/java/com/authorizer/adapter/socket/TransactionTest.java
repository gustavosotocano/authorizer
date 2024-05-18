package com.authorizer.adapter.socket;

import com.authorizer.domain.model.AccountDetail;
import com.authorizer.adapter.persistence.TransactionRepository;
import com.authorizer.port.TransactionService;
import com.authorizer.domain.model.Account;
import com.authorizer.domain.model.Transaction;
import com.authorizer.domain.model.TransactionDetail;
import com.authorizer.adapter.persistence.AccountRepository;
import com.authorizer.port.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class TransactionTest {
  @Autowired
  private AccountService        accountService;
  @Autowired
  private TransactionService    transactionService;
  @Mock
  private AccountRepository     accountRepository;
  @Mock
  private TransactionRepository transactionRepository;
  private Transaction           transaction=new Transaction();
  private Account               account=new Account();
private   int                   transactions;


  @Test
  public void aWhenAcountNotInitilized() throws JsonProcessingException {

    DateFormat formatter = new SimpleDateFormat(  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    String formattedDate = formatter.format(new Date());
    String json="{\"transaction\": {\"merchant\": \"Burger King\",\"amount\": 50,\"time\":\""+formattedDate+"\"}}";
      accountService.remove();
    //given(accountRepository.addAccount(account)).willReturn(account);
    //Mockito.when(accountRepository.getAccountById(1)).thenReturn(account);

    AuthorizerController authorizerController =new AuthorizerController(accountService,transactionService);

    json= authorizerController.executeAuthorizer(json);

    String finalJson = json;
    Assertions.assertAll("test",
            () -> assertEquals("{\"account\":null,\"violations\":[\"account-not-initialized\"]}"
                    , finalJson, "Account Not Initialized")
    );
  }
  @Test
  public void bWhenOneTransaction() throws JsonProcessingException {

    transactionService.remove();
    accountService.remove();
    DateFormat formatter = new SimpleDateFormat(  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    String formattedDate = formatter.format(new Date());
    String json="{\"transaction\": {\"merchant\": \"Burger King\",\"amount\": 1,\"time\":\""+formattedDate+"\"}}";
    transactions=0;
    AccountDetail accountDetail=new AccountDetail(1,true, 5);
    account.setAccount(accountDetail);
    accountService.addAccount(account);
 //   given(accountRepository.getAccountById(1)).willReturn(account);
    Mockito.when(accountRepository.getAccountById(1)).thenReturn(account);

    AuthorizerController authorizerController =new AuthorizerController(accountService,transactionService);

    json= authorizerController.executeAuthorizer(json);


    String finalJson = json;
    Assertions.assertAll("test",
            () -> assertEquals("{\"account\":{\"active-card\":true,\"available-limit\":4},\"violations\":[]}"
                    , finalJson, "Error Transaction ")
    );
  }
  @Test
  public void cWhenDobledTransaction() throws JsonProcessingException {
    accountService.remove();
    AccountDetail accountDetail=new AccountDetail(1,true, 12);
    account.setAccount(accountDetail);
    accountService.addAccount(account);
  //  DateFormat formatter = new SimpleDateFormat(  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
  //  String formattedDate = formatter.format(new Date());transactionService.remove();
 //   String json="{\"transaction\": {\"merchant\": \"Burger King\",\"amount\": 4,\"time\":\""+formattedDate+"\"}}";
    transactions=2;
    Account account=putTransactions(6,12,"",true);
    //   given(accountRepository.addAccount(account)).willReturn(account);

    ObjectMapper obj = new ObjectMapper();
  String  json = obj.writeValueAsString(account);


    //String finalJson = json;
    Assertions.assertAll("test",
            () -> assertEquals("{\"account\":{\"active-card\":true,\"available-limit\":6},\"violations\":[\"doubled-transaction\"]}"
                    , json, "Expected Doubled Transaction ")
    );
  }
  @Test
  public void cWhenDobledTransactionLimit() throws IOException {
    accountService.remove();
    AccountDetail accountDetail=new AccountDetail(1,true, 2);
    account.setAccount(accountDetail);
    accountService.addAccount(account);
    DateFormat formatter = new SimpleDateFormat(  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    String formattedDate = formatter.format(new Date());transactionService.remove();
    String json="{\"transaction\": {\"merchant\": \"Burger King\",\"amount\": 4,\"time\":\""+formattedDate+"\"}}";
    transactions=2;
    Account account=putTransactions(3,4,"",true);
    //   given(accountRepository.addAccount(account)).willReturn(account);

    ObjectMapper obj = new ObjectMapper();
    json = obj.writeValueAsString(account);


    String finalJson = json;
    Assertions.assertAll("test",
            () -> assertEquals("{\"account\":{\"active-card\":true,\"available-limit\":1},\"violations\":[\"doubled-transaction\",\"insufficient-limit\"]}"
                    , finalJson, "Expected Doubled Transaction ")
    );

  }

  @Test
  public void whenAvailableLimit() throws IOException {
    accountService.remove();
    AccountDetail accountDetail=new AccountDetail(1,true, 3);
    account.setAccount(accountDetail);
    accountService.addAccount(account);
    DateFormat formatter = new SimpleDateFormat(  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    String formattedDate = formatter.format(new Date());transactionService.remove();
    String json="{\"transaction\": {\"merchant\": \"Burger King\",\"amount\": 4,\"time\":\""+formattedDate+"\"}}";
    transactions=1;
    Account account=putTransactions(9,8,"",true);
    //   given(accountRepository.addAccount(account)).willReturn(account);

    ObjectMapper obj = new ObjectMapper();
    json = obj.writeValueAsString(account);

    String finalJson = json;
    Assertions.assertAll("test",
            () -> assertEquals("{\"account\":{\"active-card\":true,\"available-limit\":8},\"violations\":[\"insufficient-limit\"]}"
                    , finalJson, "Expected Doubled Transaction ")
    );
  }
  @Test
  public void whenlastThreeTransaction() throws IOException {
    accountService.remove();
    AccountDetail accountDetail=new AccountDetail(1,true, 3);
    account.setAccount(accountDetail);
    accountService.addAccount(account);
    DateFormat formatter = new SimpleDateFormat(  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    String formattedDate = formatter.format(new Date());transactionService.remove();
    String json="{\"transaction\": {\"merchant\": \"Burger King\",\"amount\": 4,\"time\":\""+formattedDate+"\"}}";
    transactions=3;
    Account account=putTransactions(8,24,"diferent",true);
    //   given(accountRepository.addAccount(account)).willReturn(account);

    ObjectMapper obj = new ObjectMapper();
    json = obj.writeValueAsString(account);

    String finalJson = json;
    Assertions.assertAll("test",
            () -> assertEquals("{\"account\":{\"active-card\":true,\"available-limit\":8},\"violations\":[\"high-frequency-small-interval\"]}"
                    , finalJson, "Expected Doubled Transaction ")
    );
  }
  @Test
  public void whenlastThreeTransactionLimit() throws IOException {
    accountService.remove();
    AccountDetail accountDetail=new AccountDetail(1,true, 3);
    account.setAccount(accountDetail);
    accountService.addAccount(account);
    DateFormat formatter = new SimpleDateFormat(  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    String formattedDate = formatter.format(new Date());transactionService.remove();
    String json="{\"transaction\": {\"merchant\": \"Burger King\",\"amount\": 4,\"time\":\""+formattedDate+"\"}}";
    transactions=3;
    Account account=putTransactions(7,20,"diferent",true);
    //   given(accountRepository.addAccount(account)).willReturn(account);

    ObjectMapper obj = new ObjectMapper();
    json = obj.writeValueAsString(account);

    String finalJson = json;
    Assertions.assertAll("test",
            () -> assertEquals("{\"account\":{\"active-card\":true,\"available-limit\":6},\"violations\":[\"high-frequency-small-interval\",\"insufficient-limit\"]}"
                    , finalJson, "Expected Doubled Transaction")
    );

  }
  @Test
  public void whenCardNotActive() throws IOException {
    accountService.remove();
    AccountDetail accountDetail=new AccountDetail(1,true, 3);
    account.setAccount(accountDetail);
    accountService.addAccount(account);
    DateFormat formatter = new SimpleDateFormat(  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    String formattedDate = formatter.format(new Date());transactionService.remove();
    String json="{\"transaction\": {\"merchant\": \"Burger King\",\"amount\": 4,\"time\":\""+formattedDate+"\"}}";
    transactions=3;
    Account account=putTransactions(7,20,"diferent",false);
    //   given(accountRepository.addAccount(account)).willReturn(account);

    ObjectMapper obj = new ObjectMapper();
    json = obj.writeValueAsString(account);


    String finalJson = json;
    Assertions.assertAll("test",
            () -> assertEquals("{\"account\":{\"active-card\":false,\"available-limit\":20},\"violations\":[\"card-not-active\"]}"
                    , finalJson, "\"Expected Doubled Transaction \"")
    );

  }
  public Account putTransactions(int amount ,int availableLimits,String diferentMerchant,boolean activeCard)  {

 AccountDetail accountDetail=new AccountDetail(1,activeCard, availableLimits);
    account.setAccount(accountDetail);
Date date=new Date();
//    accountService.addAccount(account);
    for (int i = 0; i < transactions; i++) {
     // DateFormat formatter = new SimpleDateFormat(  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    //  String formattedDate = formatter.format(new Date());
      Transaction transaction=new Transaction();
      String diferent="";
      if(!"".equals(diferentMerchant)){
        diferent=""+i;
      }
      TransactionDetail detail=new TransactionDetail( "Burger King"+diferent, amount, date);
      transaction.setTransaction(detail);
       account=transactionService.addTransaction(transaction);
     System.out.println(account.toString());
      this.transaction=transaction;
    }


return account;

  }

}

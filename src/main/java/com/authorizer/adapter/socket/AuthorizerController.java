package com.authorizer.adapter.socket;

import com.authorizer.domain.model.Account;
import com.authorizer.domain.model.Authorizer;
import com.authorizer.domain.model.Transaction;
import com.authorizer.port.AccountService;
import com.authorizer.port.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class AuthorizerController {
  Logger logger = LoggerFactory.getLogger(AuthorizerController.class);

  private static final int JUST_ONE_ACCOUNT = 1;

  @Value("${tcp-port}")
  private  String socketPort;

  private final AccountService accountService;

  public final TransactionService transactionService;


  public AuthorizerController(AccountService accountService, TransactionService transactionService) {
    this.accountService = accountService;


    this.transactionService = transactionService;
  }

  private boolean stopSocket = false;

  @PostConstruct
  private void start() {
    logger.info("start servlet ");
    Runnable runnable = () -> {

      int serverPort = Integer.parseInt(socketPort);//2222;//authorizerProperties.getTcpPort();

      try (ServerSocket serverSocket = new ServerSocket(serverPort)) {

      while (!stopSocket) {

        try {
          Socket server = serverSocket.accept();
          BufferedReader fromClient = new BufferedReader(new InputStreamReader(server.getInputStream()));
          String json = fromClient.readLine();


          logger.info("json recieved "+json);
          json = executeAuthorizer(json);
          PrintWriter toClient = new PrintWriter(server.getOutputStream(), true);
          logger.info("json response "+json);
          toClient.println(json);

        } catch (IOException e) {
          logger.error("error in controller", e);
        }
      }
      } catch (IOException e) {
        logger.error("Error in thread ", e);
      }


      logger.info("end thread ");
    };
    Thread t = new Thread(runnable);
    t.start();

  }

  public String executeAuthorizer(String json) throws JsonProcessingException {
    ObjectMapper obj = new ObjectMapper();
    Account account;

    Authorizer authorizer = mapJson(json);

    switch (authorizer) {
      case Account accountAuthorizer -> account = processAccount(accountAuthorizer);
      case Transaction transactionAuthorizer -> account = processTransaction(transactionAuthorizer);
      default -> {
        account = new Account();
        account.getViolations().add("unknown-authorizer-type");
      }
    }
    if (null == account) {
      account = new Account();
      account.getViolations().add("inbound-message-does-not-have-the-allowed-structure");
    }
    json = obj.writeValueAsString(account);
    return json;
  }


  private Account processAccount(Account account) {
    account.getAccount().setAccountId(JUST_ONE_ACCOUNT);
    return accountService.addAccount(account);
  }

  private Account processTransaction(Transaction transaction) {
    return transactionService.addTransaction(transaction);
  }

  private <T> T mapJson(String json) {
    Authorizer authorizer = null;
    ObjectMapper obj = new ObjectMapper();
    try {
      if (null == json) {
        throw new IOException("json null");
      }
      if (json.contains("account")) {
        authorizer = obj.readValue(json, Account.class);
        Account acc = (Account) authorizer;
        if (null == acc.getAccount().getActiveCard() || null == acc.getAccount().getAvailableLimit()) {
          throw new IOException("error in mapper");
        }
      } else if (json.contains("transaction")) {
        authorizer = obj.readValue(json, Transaction.class);
        Transaction tra = (Transaction) authorizer;
        if (null == tra.getTransaction().getTime() || null == tra.getTransaction().getMerchant() || null == tra.getTransaction().getAmount()) {
          throw new IOException("error in mapper");
        }
      }
    } catch (IOException e) {
      Account account = new Account();
      account.getViolations().add("inbound-message-does-not-have-the-allowed-structure");
    }
    return (T) authorizer;
  }

  @PreDestroy
  public void preDestroy() {
    logger.info("stop thread");
    stopSocket = true;

  }
}

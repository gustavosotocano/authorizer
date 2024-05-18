package com.authorizer.adapter.rest;

import com.authorizer.domain.model.Account;
import com.authorizer.domain.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONException;


import java.io.*;
import java.net.InetAddress;

import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TestSocket {

  public void testSocket() throws IOException, JSONException {

  int serverPort = 2222;
  InetAddress host = InetAddress.getByName("localhost");
  System.out.println("Connecting to server on port " + serverPort);

  Socket socket = new Socket(host, serverPort);
  //Socket socket = new Socket("127.0.0.1", serverPort);
  System.out.println("Just connected to " + socket.getRemoteSocketAddress());
  PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
  BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//toServer.println("{\"account\": {\"active-card\": true, \"available-limit\": 100}}");

  DateFormat formatter = new SimpleDateFormat(  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
  String formattedDate = formatter.format(new Date());

   //toServer.println("{\"transaction\": {\"merchant\": \"Burger King\",\"amount\": 5,\"time\": \"2019-02-13T10:03:00.000Z\"}}");
   toServer.println("{\"transaction\": {\"merchant\": \"BurgerK ing\",\"amount\": 50,\"time\": \""+formattedDate+"\"}}");
    String line = fromServer.readLine();
    System.out.println("Client received: " + line + " from Server");
    toServer.close();
    fromServer.close();
    socket.close();

  }

  public void jsonToDto() throws IOException {
    String json = "{\"account\": {\"active-card\": true, \"available-limit\": 100}}";
    // String  json="{\"active-card\": true, \"available-limit\": 100}";

    ObjectMapper obj = new ObjectMapper();
    Account account = obj.readValue(json, Account.class);
    System.out.println(account);

  }

  public void testTCP(){
  ExecutorService executorService = Executors.newFixedThreadPool(10);

  executorService.execute(new Runnable() {
    @SneakyThrows
    public void run() {
      testSocket();
    }
  });

  executorService.shutdown();




  }

  public void jsonToDtoT() throws IOException, ParseException {
    ObjectMapper obj = new ObjectMapper();
    String json = "{\"transaction\": {\"merchant\": \"Burger King\",\"amount\": 20,\"time\": \"2019-02-13T10:00:00.000Z\"}}";
    // String  json="{\"active-card\": true, \"available-limit\": 100}";

    Transaction transaction = obj.readValue(json, Transaction.class);
    Map<Integer, Transaction> transactionMap = new HashMap<Integer, Transaction>(0);

    transactionMap.put(1, transaction);

     json = "{\"transaction\": {\"merchant\": \"McDonals\",\"amount\": 5,\"time\": \"2019-02-13T10:01:00.000Z\"}}";
    transaction = obj.readValue(json, Transaction.class);

    transactionMap.put(2, transaction);
    json = "{\"transaction\": {\"merchant\": \"MrKing\",\"amount\": 5,\"time\": \"2019-02-13T10:02:00.000Z\"}}";
    transaction = obj.readValue(json, Transaction.class);
    transactionMap.put(3, transaction);
    json = "{\"transaction\": {\"merchant\": \"Frisby\",\"amount\": 5,\"time\": \"2019-02-13T10:03:00.000Z\"}}";
    transaction = obj.readValue(json, Transaction.class);
    transactionMap.put(4, transaction);
    json = "{\"transaction\": {\"merchant\": \"juan Valdez\",\"amount\": 5,\"time\": \"2019-02-13T10:04:00.000Z\"}}";
    transaction = obj.readValue(json, Transaction.class);
    transactionMap.put(5, transaction);
    json = "{\"transaction\": {\"merchant\": \"Best Buy\",\"amount\": 5,\"time\": \"2019-02-13T10:05:00.000Z\"}}";
    transaction = obj.readValue(json, Transaction.class);
    transactionMap.put(6, transaction);
    System.out.println("hora :"+new Date());
Date date=new Date(new Date().getTime()-(120 *1000));
    System.out.println("hora :"+date);

    Date sDate1=stringToDate("2019-02-13T10:03:00.000Z");


    Predicate<Date> filterGreater3 = f-> f.after(sDate1) || f.equals(sDate1) ;
  List<Transaction> a=  transactionMap.entrySet()

        .stream()
      .parallel ()
        .filter(x->filterGreater3.test(x.getValue().getTransaction().getTime()))
      .map(Map.Entry::getValue)
      .collect(Collectors.toList());


System.out.println(a.size());

  }

  public Date stringToDate(String pDate) {
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(pDate).withZoneSameInstant(ZoneId.of("America/Bogota"));
    Date date = Date.from(zonedDateTime.toInstant());

    return date;
  }

}

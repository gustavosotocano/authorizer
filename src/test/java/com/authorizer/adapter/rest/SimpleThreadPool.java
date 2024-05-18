package com.authorizer.adapter.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleThreadPool {

  public static void main(String[] args) throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(25);
    String jsonIni = "{\"account\": {\"active-card\": true, \"available-limit\": 100}}";

    Date date = new Date();
    Date sDate1=new Date(date.getTime()-(120 *1000));
    DateFormat formatter = new SimpleDateFormat(  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    for (int i = 0; i < 100; i++) {
      String formattedDate = formatter.format(sDate1);
    //  System.out.println(formattedDate);
      Runnable worker = new WorkerThread(jsonIni);
      executor.execute(worker);
      jsonIni="{\"transaction\": {\"merchant\": \"BurgerK ing"+i+"\",\"amount\": 10,\"time\": \""+formattedDate+"\"}}";
      date = new Date();
      sDate1=new Date(date.getTime()-(10 *1000));
      Thread.sleep(1*1000);
    }
    executor.shutdown();

    System.out.println("Finished all threads");
  }
}
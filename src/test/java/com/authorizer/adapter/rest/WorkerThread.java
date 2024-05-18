package com.authorizer.adapter.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class WorkerThread implements Runnable {

  private String command;

  public WorkerThread(String s){
    this.command=s;
  }

  @Override
  public void run() {
    System.out.println(" Start. Command = "+command);
    processCommand();
  //  System.out.println(Thread.currentThread().getName()+" End.");
  }

  private void processCommand() {
    try {
      int serverPort = 2222;
      InetAddress host = InetAddress.getByName("127.0.0.1");//    "172.17.0.2");
    //  System.out.println("Connecting to server on port " + serverPort);

      Socket socket = new Socket(host, serverPort);
      //Socket socket = new Socket("127.0.0.1", serverPort);
  //    System.out.println("Just connected to " + socket.getRemoteSocketAddress());
      PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      //toServer.println("{\"account\": {\"active-card\": true, \"available-limit\": 100}}" );
     // toServer.println("{\"transaction\": {\"merchant\": \"BurgerK ing\",\"amount\": 5,\"time\": \"2019-02-13T10:02:00.000Z\"}}");
      toServer.println(this.command);
      //toServer.println("{\"transaction\": {\"transaction\": \"BurgerK ing\",\"amount\": 80,\"time\": \"2019-02-13T10:02:00.000Z\"}}");
      String line = fromServer.readLine();
      System.out.println("Client received: " + line + " from Server");
      toServer.close();
      fromServer.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString(){
    return this.command;
  }
}
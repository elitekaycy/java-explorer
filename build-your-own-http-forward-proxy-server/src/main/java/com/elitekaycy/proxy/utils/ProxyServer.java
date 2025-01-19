package com.elitekaycy.proxy.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyServer implements Proxy {
  private final int port;

  public ProxyServer(int port) {
    this.port = port;
  }

  public ProxyServer() {
    this.port = 8000;
  }

  @Override
  public void start() {

    try (ServerSocket serverSocket = new ServerSocket(this.port)) {
      System.out.println("ProxyServer started on port " + this.port);
      while (true) {
        Socket client = serverSocket.accept();
        new Thread(new ProxyHandler(client)).start();
      }
    } catch (IOException ignored) {
    }
  }
}

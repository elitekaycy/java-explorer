package com.elitekaycy.proxy.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class ProxyServer implements Proxy {
  private final int port;
  private final Properties properties;

  public ProxyServer(int port) {
    this(port, ProxyProperties.readPropertiesFile("config.properties"));
  }

  public ProxyServer(int port, Properties properties) {
    this.port = port;
    this.properties = properties;
  }

  public ProxyServer() {
    this(8000, ProxyProperties.readPropertiesFile("config.properties"));
  }

  @Override
  public void start() {

    try (ServerSocket serverSocket = new ServerSocket(this.port)) {
      System.out.println("ProxyServer started on port " + this.port);
      while (true) {
        Socket client = serverSocket.accept();
        new Thread(new ProxyHandler(client, this.properties)).start();
      }
    } catch (IOException ignored) {
    }
  }
}

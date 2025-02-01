package com.elitekaycy.scanner.utils;

public class Address {

  private String host = "localhost";
  private int port = 53;

  public Address() {}

  public Address(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public static Address createAddress(String[] args) {
    Address address = new Address();
    for (String arg : args) {
      if (arg.startsWith("-host=")) {
        address.setHost(arg.split("=")[1]);
      } else if (arg.startsWith("-port=")) {
        try {
          address.setPort(Integer.parseInt(arg.split("=")[1]));
        } catch (NumberFormatException e) {
          System.out.println("Invalid port number.");
        }
      }
    }

    return address;
  }
}

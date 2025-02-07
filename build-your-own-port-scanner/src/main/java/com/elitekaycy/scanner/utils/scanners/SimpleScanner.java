package com.elitekaycy.scanner.utils.scanners;

import com.elitekaycy.scanner.utils.Address;
import java.io.IOException;
import java.net.Socket;

// Scans a port (just one)
public class SimpleScanner implements Scanner {

  private final Address address;

  public SimpleScanner(Address address) {
    this.address = address;
  }

  public Address getAddress() {
    return this.address;
  }

  @Override
  public void scan() {
    try (Socket socketScan = new Socket(address.getHost(), address.getPort())) {
      System.out.println("port open on " + address.getHost() + " " + address.getPort());
    } catch (IOException ex) {
      System.out.println(
          "port not open" + address.getHost() + " " + address.getPort() + " " + ex.getMessage());
    }
  }
}

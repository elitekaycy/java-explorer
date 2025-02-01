package com.elitekaycy.scanner.utils.scanners;

import com.elitekaycy.scanner.utils.Address;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// works similarly to the SimpleScanner but runs all port from the starting port in multiple threads
public class VanillaScanner extends SimpleScanner implements Scanner {
  private int threads = 3;

  public VanillaScanner(Address address) {
    super(address);
  }

  public VanillaScanner(Address address, int threads) {
    this(address);
    this.threads = threads;
  }

  @Override
  public void scan() {
    ExecutorService executor = Executors.newFixedThreadPool(threads);
    for (int i = this.getAddress().getPort(); i <= 65000; i++) {
      final int port = i;
      executor.submit(
          () -> {
            scanAddr(new Address(this.getAddress().getHost(), port));
          });
    }
  }

  protected void scanAddr(Address address) {
    try (Socket socketScan = new Socket(address.getHost(), address.getPort())) {
      System.out.println("port open on " + address.getHost() + " " + address.getPort());
    } catch (IOException ex) {
      System.out.println(
          "port not open" + address.getHost() + " " + address.getPort() + " " + ex.getMessage());
    }
  }
}

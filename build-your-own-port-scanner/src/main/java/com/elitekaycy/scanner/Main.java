package com.elitekaycy.scanner;

import com.elitekaycy.scanner.utils.Address;
import com.elitekaycy.scanner.utils.scanners.Scanner;
import com.elitekaycy.scanner.utils.scanners.SweepScanner;

public class Main {
  public static void main(String[] args) {

    for (String arg : args) System.out.println(arg);
    Address scannerAddress = Address.createAddress(args);
    // Scanner scanner = new SimpleScanner(scannerAddress);
    // Scanner scanner = new VanillaScanner(scannerAddress);
    Scanner scanner = new SweepScanner(scannerAddress);
    scanner.scan();
  }
}

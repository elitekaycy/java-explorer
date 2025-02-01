package com.elitekaycy.scanner.utils.scanners;

import com.elitekaycy.scanner.utils.Address;

// Similar to vanilla scan but works on multiple host
public class SweepScanner extends VanillaScanner implements Scanner {

  public SweepScanner(Address address) {
    super(address);
  }

  @Override
  public void scan() {
    scanAddr(this.getAddress());
  }

  protected void scanAddr(Address address) {
    if (super.getAddress().getHost().contains("*")) {
      String base =
          super.getAddress()
              .getHost()
              .substring(0, super.getAddress().getHost().lastIndexOf('.') + 1);
      for (int i = 1; i <= 255; i++) {
        String host = base + i;
        new VanillaScanner(new Address(host, address.getPort())).scan();
      }
    } else {
      String[] hostList = super.getAddress().getHost().split(",");
      for (String host : hostList) {
        new VanillaScanner(new Address(host, address.getPort())).scan();
      }
    }
  }
}

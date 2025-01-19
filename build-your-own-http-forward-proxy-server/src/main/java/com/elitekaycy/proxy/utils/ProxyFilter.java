package com.elitekaycy.proxy.utils;

import java.util.Properties;

public class ProxyFilter {
  private final Properties properties;

  public ProxyFilter(Properties properties) {
    this.properties = properties;
  }

  public void filterByBannedIps(String host) {
    String[] ipWhitelist = properties.getProperty("banned.ips").split(",");
    System.out.println("host " + host);
    for (String ip : ipWhitelist) {
      System.out.println("ip whitelist " + ip);
      if (host.equals(ip)) {
        throw new ProxyFilterException("blocked Ip " + ip);
      }
    }
  }

  public static void filterByBannedWords() {}
}

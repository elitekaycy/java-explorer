package com.elitekaycy.proxy;

import com.elitekaycy.proxy.utils.*;
import java.util.Properties;

public class Main {
  public static void main(String[] args) {
    Properties proxyProperties = ProxyProperties.loadPropertiesFromArgs(args);

    int port =
        proxyProperties.getProperty("server.port") != null
            ? Integer.parseInt(proxyProperties.getProperty("server.port"))
            : 8000;

    new ProxyServer(port, proxyProperties).start();
  }
}

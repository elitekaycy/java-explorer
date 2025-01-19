package com.elitekaycy.proxy.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ProxyProperties {

  public static Properties loadPropertiesFromArgs(String[] args) {
    Properties proxyProperties;
    for (String arg : args) System.out.println(arg);
    if (args.length > 1 && args[1] != null) {
      proxyProperties = readPropertiesFile(args[1]);
    } else {
      proxyProperties = readPropertiesFile("config.properties");
    }
    return proxyProperties;
  }

  public static Properties readPropertiesFile(String filePath) {

    Properties properties = new Properties();
    try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
      properties.load(fileInputStream);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return properties;
  }
}

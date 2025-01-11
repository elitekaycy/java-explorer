package org.lb.util.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.lb.util.Server;

public class ConfigProperties {

  private static int port;
  private static List<Server> servers;
  private static Properties properties = new Properties();

  static {
    servers = new ArrayList<>();
    String configFilePath = System.getProperty("config");
    if (configFilePath != null) {

      try (FileInputStream input = new FileInputStream(configFilePath)) {
        properties.load(input);
        System.out.println("Loaded properties from config file: " + configFilePath);
      } catch (IOException e) {
        System.err.println("Failed to load properties from file: " + e.getMessage());
        System.exit(1);
      }
    }

    System.getProperties().forEach((key, value) -> properties.put(key, value));

    port = Integer.parseInt(properties.getProperty("port", "8080"));
    String servers = properties.getProperty("servers", "");
    setServers(servers);

    System.out.println(configFilePath + " ==> " + port + " ==> " +  servers); 
 }

  private static void setServers(String serverStr) {
    String[] serverss = serverStr.split(",");
    for (String server : serverss) {
      String[] serverSplit = server.split(":");
      int port = 80;
      if (serverSplit.length == 2) {
        port = Integer.parseInt(serverSplit[1]);
      }
      servers.add(new Server(serverSplit[0], (int) port));
    }
  }

  public static List<Server> getServers() {
    return servers;
  }

  public static int getPort() {
    return port;
  }
}

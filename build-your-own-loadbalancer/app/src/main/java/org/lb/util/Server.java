package org.lb.util;

public class Server {
  private final int port;
  private final String host;
  private boolean healthy = true;

  public Server(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public synchronized boolean isHealthy() {
    return healthy;
  }

  public synchronized void setHealthy(boolean healthy) {
    this.healthy = healthy;
  }

  @Override
  public String toString() {
    return host + ":" + port;
  }
}

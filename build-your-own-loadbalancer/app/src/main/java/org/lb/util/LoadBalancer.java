package org.lb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.lb.util.interfaces.Strategy;
import org.lb.util.strategy.RoundRobinStrategy;

public class LoadBalancer {
  private CopyOnWriteArrayList<Server> servers = new CopyOnWriteArrayList<>();
  private AtomicInteger nextServerIdx = new AtomicInteger(0);
  private int port;
  private int healthCheckInterval = 10;
  private Strategy strategy;
  private HttpHealthChecker healthChecker = new HttpHealthChecker();

  public LoadBalancer(int port, Strategy strategy) {
    this.port = port;
    this.strategy = strategy;
  }

  public LoadBalancer(int port) {
    this(port, new RoundRobinStrategy());
  }

  public LoadBalancer() {
    this(8080);
  }

  public void addServer(Server server) {
    this.servers.add(server);
  }

  public void addServer(String host, int port) {
    addServer(new Server(host, port));
  }

  public void addServers(List<Server> servers) {
    for (Server server : servers) {
      addServer(server);
    }
  }

  public void start() throws IOException {
    Executors.newSingleThreadScheduledExecutor()
        .scheduleAtFixedRate(this::healthCheck, 0, healthCheckInterval, TimeUnit.SECONDS);

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("loadbalancer started on port " + port);

      while (true) {
        Socket client = serverSocket.accept();
        handleClient(client);
      }
    }
  }

  private void handleClient(Socket client) {
    new Thread(
            () -> {
              try (PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                  BufferedReader in =
                      new BufferedReader(new InputStreamReader(client.getInputStream()))) {

                String request = in.readLine();
                if (request == null || servers.isEmpty()) {
                  out.println("HTTP/1.1 503 Service Unavailable\r\n");
                  return;
                }

                Server server = strategy.getNextServer(servers);
                if (server != null) {
                  String response = forwardRequest(server, request);
                  out.println(response);
                } else {
                  out.println("HTTP/1.1 503 Service Unavailable\r\n");
                }

              } catch (IOException ignored) {
              }
            })
        .start();

    ;
  }

  private String forwardRequest(Server server, String request) {
         try (Socket backendSocket = new Socket(server.getHost(), server.getPort());
             PrintWriter backendOut = new PrintWriter(backendSocket.getOutputStream(), true);
             BufferedReader backendIn = new BufferedReader(new InputStreamReader(backendSocket.getInputStream()))) {

            backendOut.println(request);
            backendOut.println();

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = backendIn.readLine()) != null) {
                response.append(line).append("\r\n");
            }
            return response.toString();
        } catch (IOException e) {
            System.err.println("Failed to forward request to server " + server);
            return "HTTP/1.1 500 Internal Server Error\r\n";
        }
  }

  private void healthCheck() {
    for (Server server : servers) {
      healthChecker.isHealthy(server);
    }
  }
}

package com.elitekaycy.proxy.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyHandler implements Runnable {
  private final Socket client;

  private static final List<String> HOP_BY_HOP_HEADERS =
      Arrays.asList(
          "connection",
          "keep-alive",
          "proxy-authenticate",
          "proxy-authorization",
          "te",
          "transfer-encoding",
          "upgrade");

  public ProxyHandler(Socket client) {
    this.client = client;
  }

  @Override
  public void run() {

    // output stream and input stream from the client
    // output stream and input stream from the forwarded

    // extract method and url
    // filter banned ip list
    // filter banned words
    // remove hop by headers
    // add x-forwaded header
    // write output to console

    try (InputStream clientInput = client.getInputStream();
        OutputStream clientOutput = client.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientInput));
        PrintWriter clientWriter = new PrintWriter(clientOutput, true); ) {

      Request req = extractRequest(reader);
      System.out.println(req);

      String clientIp = client.getInetAddress().getHostAddress();
      req.headers.put(
          "x-forwarded-for", req.headers.getOrDefault("x-forwarded-for", "") + clientIp);

      HOP_BY_HOP_HEADERS.forEach(req.headers::remove);
      forwardRequest(req, clientWriter);

    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private Request extractRequest(BufferedReader reader) throws IOException {
    String requestLine = reader.readLine();
    if (requestLine == null || requestLine.isEmpty()) {
      return null;
    }
    System.out.println("Request: " + requestLine);

    String[] requestPaths = requestLine.split(" ");
    String method = requestPaths[0];
    String url = requestPaths[1];

    Map<String, String> reqHeaders = new HashMap<>();
    String host = null;
    int port = 80;
    String line;

    while (!(line = reader.readLine()).isEmpty()) {
      int seperator = line.indexOf(":");

      if (seperator > 0) {
        String headerName = line.substring(0, seperator).trim().toLowerCase();
        String headerValue = line.substring(seperator + 1).trim().toLowerCase();
        reqHeaders.put(headerName, headerValue);

        if (headerName.equals("host")) {
          host = headerValue.contains(":") ? headerValue.split(":")[0] : headerValue;
          port = headerValue.contains(":") ? Integer.parseInt(headerValue.split(":")[1]) : 80;
        }
      }
    }

    if (host == null) {
      System.err.println("No Host header found!");
      return null;
    }

    return new Request(method, url, host, port, reqHeaders);
  }

  private void filterByBannedIps() {}

  private void filterByBannedWords() {}

  private void forwardRequest(Request request, PrintWriter writer) throws IOException {
    try (Socket destination = new Socket(request.host(), request.port());
        InputStream destInput = destination.getInputStream();
        OutputStream destOutput = destination.getOutputStream();
        BufferedWriter destWriter = new BufferedWriter(new OutputStreamWriter(destOutput));
        BufferedReader destReader = new BufferedReader(new InputStreamReader(destInput)); ) {

      try {
        destWriter.write(request.method() + " " + request.url() + " HTTP/1.1\r\n");
        request
            .headers()
            .forEach(
                (key, value) -> {
                  try {
                    destWriter.write(key + ": " + value + "\r\n");
                  } catch (IOException ignored) {
                  }
                });

        destWriter.write("\r\n");
        destWriter.flush();
      } catch (IOException ignored) {
      }
      String line;
      while ((line = destReader.readLine()) != null) {
        writer.println(line);
      }

    } catch (IOException ignored) {
    }
  }

  static record Request(
      String method, String url, String host, int port, Map<String, String> headers) {
    @Override
    public String toString() {
      return "Request{"
          + "method='"
          + method
          + '\''
          + ", url='"
          + url
          + '\''
          + ", host='"
          + host
          + '\''
          + ", port="
          + port
          + ", headers="
          + headers
          + '}';
    }
  }
}

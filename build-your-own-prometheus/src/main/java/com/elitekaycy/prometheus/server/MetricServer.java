package com.elitekaycy.prometheus.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.elitekaycy.prometheus.models.Metric;
import com.elitekaycy.prometheus.store.MetricStore;

import main.java.com.elitekaycy.prometheus.ui.WebUi;

import com.elitekaycy.prometheus.query.QueryEngine;
import com.elitekaycy.prometheus.query.QueryEngine.Query;

public class MetricServer {
    private int port;
    private static MetricStore registry = MetricStore.getInstance();

    public MetricServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Prometheus Server running on http://localhost:" + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClientRequest(clientSocket);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private static void handleClientRequest(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            String requestLine = in.readLine();
            if (requestLine == null)
                return;

            String[] tokens = requestLine.split(" ");
            if (tokens.length < 2)
                return;

            String method = tokens[0];
            String path = tokens[1];

            String response = switch (path) {
                case "/metrics" -> registry.exportMetrics();
                case "/api/metrics" -> registry.exportJson();
                case "/dashboard" -> serveDashboard();
                case "/query" -> handleQueryRequest(path);
                default -> "HTTP/1.1 404 Not Found\r\n\r\n";
            };

            String contentType = switch (path) {
                case "/dashboard" -> "text/html";
                default -> "text/plain";
            };

            out.write("HTTP/1.1 200 OK\r\n");
            out.write("Content-Type: " + contentType + "\r\n");
            out.write("Content-Length: " + response.length() + "\r\n");
            out.write("\r\n");
            out.write(response);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String handleQueryRequest(String path) {
        String metricName = null;
        long start = 0;
        long end = MetricStore.getInstance().getLength();

        String[] params = path.split("&");
        for (String param : params) {
            if (param.startsWith("name=")) {
                metricName = param.split("=")[1];
            } else if (param.startsWith("start=")) {
                start = Long.parseLong(param.split("=")[1]);
            } else if (param.startsWith("end=")) {
                end = Long.parseLong(param.split("=")[1]);
            }
        }
        if (metricName == null) {
            return "No metric name provided";
        }

        QueryEngine.Query query = new QueryEngine.Query(metricName, start, end);
        List<Metric> metrics = QueryEngine.filterByTimeRange(query);

        if (metrics.isEmpty()) {
            return "Empty Response";
        }

        return registry.exportJson(metrics);
    }

    private static String serveDashboard() {
        return WebUi.serveUi();
    }

}

package com.elitekaycy.prometheus.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.elitekaycy.prometheus.store.MetricStore;
import com.elitekaycy.prometheus.models.Metric;

import com.elitekaycy.prometheus.alert.AlertEngine;
import com.elitekaycy.prometheus.utils.PrometheusConfig;

public class MetricJobRunner {
    private PrometheusConfig config;
    private static MetricStore store = MetricStore.getInstance();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Pattern METRIC_PATTERN = Pattern.compile(
            "(\\w+)(\\{.*?})?\\s+([-+]?\\d*\\.?\\d+(?:[eE][-+]?\\d+)?)\\s*(\\d+)?");

    public MetricJobRunner(PrometheusConfig config) {
        this.config = config;
    }

    public void run() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                pull();
                AlertEngine.evaluateMetrics(config.getAlertRules(), config.getAlertLog());
            } catch (Exception ex) {
                System.out.println("Error scraping metrics " + ex.getMessage());
            }

        }, 0, (long) Math.floor(config.getScrapeInterval() / 1000), TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down metrics scraper...");
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Forcing shutdown...");
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }));
    }

    private void pull() throws Exception {
        URL url = new URI(config.getMetricsPath()).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }

                Matcher matcher = METRIC_PATTERN.matcher(line);
                if (matcher.matches()) {
                    String name = matcher.group(1);
                    String labelString = matcher.group(2);
                    double value = Double.parseDouble(matcher.group(3));
                    long timestamp = matcher.group(4) != null ? Long.parseLong(matcher.group(4))
                            : System.currentTimeMillis();

                    Map<String, String> labels = parseLabels(labelString);
                    Metric metric = new Metric(name, labels, value, timestamp);
                    System.out.println("metric -> " + metric);
                    store.storeMetrics(metric);
                }
            }
            System.out.println("Scraped " + store.getLength() + " metrics so far.");
        } catch (Exception e) {
            System.err.println("Failed to read metrics: " + e.getMessage());
        }
    }

    private static Map<String, String> parseLabels(String labelString) {
        Map<String, String> labels = new HashMap<>();
        if (labelString != null && labelString.startsWith("{") && labelString.endsWith("}")) {
            String content = labelString.substring(1, labelString.length() - 1);
            String[] pairs = content.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    labels.put(keyValue[0].trim(), keyValue[1].replace("\"", "").trim());
                }
            }
        }
        return labels;
    }
}

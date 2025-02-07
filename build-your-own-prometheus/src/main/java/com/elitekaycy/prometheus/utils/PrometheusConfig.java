package com.elitekaycy.prometheus.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import com.elitekaycy.prometheus.models.Metric;

public class PrometheusConfig {
    private int scrapeInterval;
    private int metricsPort;
    private int port;
    private String metricsPath;
    private String storagePath;
    private String alertManagerUrl;
    private String alertLog;
    private static List<AlertRule> alertRules = new ArrayList<>();

    public PrometheusConfig(String filePath) {
        loadConfig(filePath);
    }

    private void loadConfig(String filePath) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);

            this.scrapeInterval = Integer.parseInt(properties.getProperty("scrape.interval", "5000"));
            this.metricsPort = Integer.parseInt(properties.getProperty("metrics.port", "8080"));
            this.metricsPath = properties.getProperty("metrics.path", "/metrics");
            this.storagePath = properties.getProperty("storage.path", "/var/prometheus/data");
            this.alertManagerUrl = properties.getProperty("alertmanager.url", "http://localhost:9093");
            this.alertLog = properties.getProperty("alert.log", "alert.log");
            this.port = Integer.parseInt(properties.getProperty("port", "9090"));

            for (String key : properties.stringPropertyNames()) {

                if (key.startsWith("alert.type.")) {
                    String rule = properties.getProperty(key);
                    alertRules.add(AlertRule.parse(rule));
                }
            }

        } catch (IOException e) {
            System.err.println("💥 Oops! Error loading config: " + e.getMessage());
        }
    }

    public int getScrapeInterval() {
        return scrapeInterval;
    }

    public int getMetricsPort() {
        return metricsPort;
    }

    public String getMetricsPath() {
        return metricsPath;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getAlertManagerUrl() {
        return alertManagerUrl;
    }

    public String getAlertLog() {
        return alertLog;
    }

    public int getPort() {
        return port;
    }

    public List<AlertRule> getAlertRules() {
        return alertRules;
    }

    @Override
    public String toString() {
        return "🌍 PrometheusConfig{" +
                "⏳ scrapeInterval=" + scrapeInterval +
                ", 🔌 metricsPort=" + metricsPort +
                ", 🔍 metricsPath='" + metricsPath + '\'' +
                ", 💾 storagePath='" + storagePath + '\'' +
                ", 🚨 alertManagerUrl='" + alertManagerUrl + '\'' +
                '}';
    }

    public static class AlertRule {

        public boolean isGreaterThan;
        public boolean isEqualTo;
        public String metricName;
        public long metricValue;

        public AlertRule(String metricName, boolean sign, boolean equal, long metricValue) {
            this.metricName = metricName;
            this.isGreaterThan = sign;
            this.isEqualTo = equal;
            this.metricValue = metricValue;
        }

        static AlertRule parse(String rule) {
            String[] rules = rule.split(" ");
            boolean equal = false;
            boolean greaterThan = false;
            if (rules[1] == ">=") {
                equal = true;
                greaterThan = true;
            } else if (rules[1] == "<=") {
                equal = true;
            } else if (rules[1] == ">") {
                greaterThan = true;
            }
            return new AlertRule((String) rules[0], greaterThan, equal, (long) Long.parseLong(rules[2]));
        }

    }
}

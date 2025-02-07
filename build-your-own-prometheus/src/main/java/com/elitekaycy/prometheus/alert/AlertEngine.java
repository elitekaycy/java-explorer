package com.elitekaycy.prometheus.alert;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.elitekaycy.prometheus.models.Metric;
import com.elitekaycy.prometheus.store.MetricStore;

import com.elitekaycy.prometheus.utils.PrometheusConfig.AlertRule;

public class AlertEngine {

    public static void evaluateMetrics(List<AlertRule> rules, String filePath) {
        for (AlertRule rule : rules) {
            List<Metric> metrics = MetricStore.getInstance().queryMetrics(rule.metricName);
            for (Metric metric : metrics) {
                if (isTriggered(rule, metric)) {
                    logAlert("🚨 ALERT: " + rule.metricName + " exceeded " + rule.metricValue + " at "
                            + metric.getTimestamp(), filePath);
                }
            }
        }
    }

    private static void logAlert(String message, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(message + "\n");
            System.out.println("Logged alert: " + message);
        } catch (IOException e) {
            System.err.println("Failed to log alert: " + e.getMessage());
        }
    }

    private static boolean isTriggered(AlertRule rule, Metric metric) {

        if (rule.metricName == metric.getName()) {
            if (rule.isGreaterThan && rule.isEqualTo && rule.metricValue >= metric.getValue())
                return true;
            if (rule.isGreaterThan && rule.metricValue > metric.getValue())
                return true;
            if (!rule.isGreaterThan && rule.isEqualTo && metric.getValue() >= rule.metricValue)
                return true;
            if (!rule.isGreaterThan && rule.metricValue < metric.getValue())
                return true;
        }
        return false;
    }

}

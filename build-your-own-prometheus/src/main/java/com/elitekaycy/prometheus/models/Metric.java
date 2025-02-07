package com.elitekaycy.prometheus.models;

import java.util.Map;

public class Metric {

    private String name;
    private Map<String, String> labels;
    private double value;
    private long timestamp;

    public Metric(String name, Map<String, String> labels, double value, long timestamp) {
        this.name = name;
        this.labels = labels;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public double getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        StringBuilder labelStr = new StringBuilder();
        if (!labels.isEmpty()) {
            labelStr.append("{");
            labels.forEach((key, value) -> labelStr.append(key).append("=\"").append(value).append("\","));
            labelStr.setLength(labelStr.length() - 1);
            labelStr.append("}");
        }
        return name + labelStr + " " + value + " " + timestamp;
    }

    public String toJson() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        jsonBuilder.append("  \"name\": \"").append(name).append("\",\n");
        jsonBuilder.append("  \"labels\": {\n");
        for (Map.Entry<String, String> entry : labels.entrySet()) {
            jsonBuilder.append("    \"").append(entry.getKey()).append("\": \"").append(entry.getValue())
                    .append("\",\n");
        }
        if (!labels.isEmpty()) {
            jsonBuilder.setLength(jsonBuilder.length() - 2);
        }
        jsonBuilder.append("  },\n");
        jsonBuilder.append("  \"value\": ").append(value).append(",\n");
        jsonBuilder.append("  \"timestamp\": ").append(timestamp).append("\n");
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

}

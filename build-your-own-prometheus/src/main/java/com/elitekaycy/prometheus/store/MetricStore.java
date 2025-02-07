package com.elitekaycy.prometheus.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.elitekaycy.prometheus.models.Metric;

interface IMetricStore {
    void storeMetrics(Metric metric);

    List<Metric> queryMetrics(String name);

    String exportMetrics();

    String exportJson();
}

public class MetricStore implements IMetricStore {
    private Map<String, List<Metric>> store = new ConcurrentHashMap<>();

    private static final MetricStore INSTANCE = new MetricStore();

    public MetricStore() {
    }

    public static MetricStore getInstance() {
        return INSTANCE;
    }

    public int getLength() {
        return store.size();
    }

    @Override
    public String exportMetrics() {
        StringBuilder metrics = new StringBuilder();
        for (List<Metric> ms : store.values()) {
            for (Metric m : ms) {
                metrics.append(m.toString()).append("\n");
            }
        }
        return metrics.toString();
    }

    @Override
    public String exportJson() {
        return "{ \"metrics\": [" +
                store.entrySet().stream()
                        .flatMap(entry -> entry.getValue().stream())
                        .map(Metric::toJson)
                        .collect(Collectors.joining(","))
                +
                "] }";
    }

    public String exportJson(List<Metric> metrics) {
        return "{ \"metrics\": [" +
                metrics.stream()
                        .map(Metric::toJson)
                        .collect(Collectors.joining(","))
                +
                "] }";
    }

    @Override
    public List<Metric> queryMetrics(String name) {
        return store.getOrDefault(name, new ArrayList<>());
    }

    @Override
    public void storeMetrics(Metric metric) {
        store.computeIfAbsent(metric.getName(), k -> new ArrayList<>()).add(metric);
    }
}

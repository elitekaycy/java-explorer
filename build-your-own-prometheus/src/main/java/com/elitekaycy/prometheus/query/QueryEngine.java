package com.elitekaycy.prometheus.query;

import java.util.List;
import java.util.stream.Collectors;

import com.elitekaycy.prometheus.models.Metric;
import com.elitekaycy.prometheus.store.MetricStore;

public class QueryEngine {

    public static List<Metric> queryMetricsByName(String name) {
        return MetricStore.getInstance().queryMetrics(name);
    }

    public static List<Metric> filterByTimeRange(Query q) {
        return MetricStore.getInstance().queryMetrics(q.getName()).stream()
                .filter(m -> m.getTimestamp() >= q.getStart() && m.getTimestamp() <= q.getEnd())
                .collect(Collectors.toList());
    }

    public static class Query {
        private String name;
        private long start;
        private long end;

        public Query(String name) {
            this.name = name;
            this.start = 0;
            this.end = MetricStore.getInstance().getLength();
        }

        public Query(String name, long start, long end) {
            this.name = name;
            this.start = start;
            this.end = end;
        }

        public String getName() {
            return name;
        }

        public long getStart() {
            return start;
        }

        public long getEnd() {
            return end;
        }
    }
}

package com.elitekaycy.prometheus;

import java.io.IOException;

import com.elitekaycy.prometheus.server.MetricServer;
import com.elitekaycy.prometheus.utils.MetricJobRunner;

import com.elitekaycy.prometheus.utils.PrometheusConfig;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from com.elitekaycy.prometheus!");
        String configFilePath = null;

        for (String arg : args) {
            if (arg.startsWith("--config=")) {
                configFilePath = arg.split("=")[1];
                break;
            }
        }

        if (configFilePath == null) {
            System.err.println(
                    "Error: Missing --config argument! Usage: java PrometheusApp --config=prometheus.properties");
            System.exit(1);
        }

        PrometheusConfig config = new PrometheusConfig(configFilePath);

        System.out.println(config);

        new Thread(() -> {
            new MetricJobRunner(config).run();
        }).start();

        new MetricServer(config.getPort()).start();

    }
}

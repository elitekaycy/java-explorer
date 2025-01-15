package org.lb.util.interfaces;

import org.lb.util.Server;

public interface HealthChecker {

  boolean isHealthy(Server server);
}

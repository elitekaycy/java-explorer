package org.lb.util;

import org.lb.util.interfaces.HealthChecker;

public class HttpHealthChecker implements HealthChecker {

  @Override
  public boolean isHealthy(Server server) {
    return true;
  }
}

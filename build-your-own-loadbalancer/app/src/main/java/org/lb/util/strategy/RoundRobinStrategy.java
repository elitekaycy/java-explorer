package org.lb.util.strategy;

import java.util.List;
import org.lb.util.Server;
import org.lb.util.interfaces.Strategy;

public class RoundRobinStrategy implements Strategy {
  private int currentIdx = -1;

  @Override
  public synchronized Server getNextServer(List<Server> servers) {
    if (servers.isEmpty()) return null;
    currentIdx = (currentIdx + 1) % servers.size();
    return servers.get(currentIdx);
  }
}

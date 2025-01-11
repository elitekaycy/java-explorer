package org.lb.util.interfaces;

import java.util.List;
import org.lb.util.Server;

public interface Strategy {
  Server getNextServer(List<Server> servers);
}

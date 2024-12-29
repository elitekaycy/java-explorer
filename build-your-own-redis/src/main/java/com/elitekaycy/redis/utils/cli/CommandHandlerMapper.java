package utils.cli;

import java.util.HashMap;
import java.util.Map;
import utils.resp.RespValue;
import utils.store.RedisStore;
import utils.store.impl.RedisStoreImpl;

public class CommandHandlerMapper {

  private Map<String, Command> handler = new HashMap<>();
  private final RedisStore store;

  public CommandHandlerMapper(RedisStore store) {
    this.store = store;
    handler.put("PING", CommandHandlerMapper::ping);
    handler.put("SET", new SetCommand(this.store));
    handler.put("GET", new GetCommand(this.store));
    handler.put("HGET", new HgetCommand(this.store));
    handler.put("HSET", new HsetCommand(this.store));
  }

  public CommandHandlerMapper() {
    this.store = RedisStoreImpl.getInstance();
    handler.put("PING", CommandHandlerMapper::ping);
    handler.put("SET", new SetCommand(this.store));
    handler.put("GET", new GetCommand(this.store));
    handler.put("HGET", new HgetCommand(this.store));
    handler.put("HSET", new HsetCommand(this.store));
  }

  public Command getHandler(String name) {
    return handler.get(name);
  }

  private static RespValue ping(RespValue[] args) {
    return new PingCommand().execute(args);
  }
}

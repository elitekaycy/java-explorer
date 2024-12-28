package utils.cli;

import utils.resp.RespType;
import utils.resp.RespValue;
import utils.store.RedisStore;

public class SetCommand implements Command {
  private final RedisStore store;

  public SetCommand(RedisStore store) {
    this.store = store;
  }

  @Override
  public RespValue execute(RespValue... args) {
    if (args.length != 2) {
      return new RespValue(RespType.ERROR, "ERR wrong number of arguments for 'set' command");
    }
    String key = args[0].bulk;
    String value = args[1].bulk;
    store.set(key, value);
    return new RespValue(RespType.SIMPLE, "OK");
  }
}

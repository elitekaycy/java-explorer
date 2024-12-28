package utils.cli;

import utils.resp.RespType;
import utils.resp.RespValue;
import utils.store.RedisStore;

public class GetCommand implements Command {
  private final RedisStore store;

  public GetCommand(RedisStore store) {
    this.store = store;
  }

  @Override
  public RespValue execute(RespValue... args) {
    if (args.length != 1) {
      return new RespValue(RespType.ERROR, "ERR wrong number of arguments for 'get' command");
    }
    String key = args[0].bulk;
    String value = store.get(key);
    if (value == null) {
      return new RespValue("null");
    }
    return new RespValue(RespType.BULK, value);
  }
}

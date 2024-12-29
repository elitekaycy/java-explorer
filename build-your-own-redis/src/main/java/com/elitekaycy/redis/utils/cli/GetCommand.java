package utils.cli;

import utils.resp.RespType;
import utils.resp.RespValue;
import utils.store.RedisStore;

/**
 * Command implementation for the Redis 'GET' operation.
 *
 * <p>Retrieves the value of the specified key. If the key does not exist, returns a 'null'
 * response.
 */
public class GetCommand implements Command {
  private final RedisStore store;

  public GetCommand(RedisStore store) {
    this.store = store;
  }

  /**
   * Executes the 'GET' command.
   *
   * <p>Retrieves the value of the specified key. If the key exists, returns the value as a bulk
   * string; if the key is not found, returns a 'null' response.
   *
   * @param args The argument for the 'GET' command: the key.
   * @return A response with the value of the key or a 'null' response if the key does not exist.
   */
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

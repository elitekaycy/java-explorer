package utils.cli;

import utils.resp.RespType;
import utils.resp.RespValue;
import utils.store.RedisStore;

/**
 * Command implementation for the Redis 'HGET' operation.
 *
 * <p>Retrieves the value of a specified field from a hash in the Redis store. If the number of
 * arguments is incorrect or the key is empty, an error response is returned.
 */
public class HgetCommand implements Command {
  private final RedisStore store;

  public HgetCommand(RedisStore store) {
    this.store = store;
  }

  /**
   * Executes the 'HGET' command.
   *
   * <p>Retrieves the value of a field from the specified hash. Returns an error if the number of
   * arguments is incorrect or if the key is empty.
   *
   * @param args The arguments for the 'HGET' command. The first is the hash, and the second is the
   *     field key.
   * @return The value associated with the field key or an error message.
   */
  @Override
  public RespValue execute(RespValue... args) {
    if (args.length != 2) {
      return new RespValue(RespType.ERROR, "ERR wrong number of arguments for 'hget' command");
    }
    String hash = args[0].bulk;
    String key = args[1].bulk;

    String v = store.hget(hash, key);
    if (v.isEmpty() || v.isBlank()) {
      return new RespValue(RespType.ERROR, "empty key");
    }
    return new RespValue(RespType.BULK, v);
  }
}

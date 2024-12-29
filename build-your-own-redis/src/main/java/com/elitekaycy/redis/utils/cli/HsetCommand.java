package utils.cli;

import java.util.ArrayList;
import java.util.List;
import utils.resp.RespType;
import utils.resp.RespValue;
import utils.store.RedisStore;

/**
 * Command implementation for the Redis 'HSET' operation.
 *
 * <p>Sets the value of a specified field in a hash. If successful, returns a response with the
 * 'HSET' command and the arguments used.
 */
public class HsetCommand implements Command {
  private final RedisStore store;

  public HsetCommand(RedisStore store) {
    this.store = store;
  }

  /**
   * Executes the 'HSET' command.
   *
   * <p>Sets the value of a field in the specified hash. Returns a response with the 'HSET' command
   * and the provided arguments.
   *
   * @param args The arguments for the 'HSET' command: the hash, field, and value.
   * @return A response with the 'HSET' command and the arguments, or an error if the number of
   *     arguments is incorrect.
   */
  @Override
  public RespValue execute(RespValue... args) {
    if (args.length != 3) {
      return new RespValue(RespType.SIMPLE, "ERR wrong number of arguments for 'hset' command");
    }
    String hash = args[0].bulk;
    String key = args[1].bulk;
    String value = args[2].bulk;

    store.hset(hash, key, value);

    List<RespValue> argValue = new ArrayList<>();

    argValue.add(new RespValue(RespType.BULK, "HSET"));
    for (RespValue val : args) {
      argValue.add(val);
    }
    return new RespValue(argValue);
  }
}

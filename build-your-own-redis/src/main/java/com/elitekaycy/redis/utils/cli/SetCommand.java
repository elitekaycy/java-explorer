package utils.cli;

import java.util.ArrayList;
import java.util.List;
import utils.resp.RespType;
import utils.resp.RespValue;
import utils.store.RedisStore;

/**
 * Command implementation for the Redis 'SET' operation.
 *
 * <p>Sets the value of the specified key. If the key already exists, it will overwrite the existing
 * value.
 *
 */
public class SetCommand implements Command {
  private final RedisStore store;

  public SetCommand(RedisStore store) {
    this.store = store;
  }

  /**
   * Executes the 'SET' command.
   *
   * <p>Sets the specified value for the provided key. If the key already exists, it overwrites the
   * value.
   *
   * @param args The arguments for the 'SET' command: the key and the value to be set.
   * @return A response indicating the operation and the arguments passed.
   */
  @Override
  public RespValue execute(RespValue... args) {
    if (args.length != 2) {
      return new RespValue(RespType.ERROR, "ERR wrong number of arguments for 'set' command");
    }
    String key = args[0].bulk;
    String value = args[1].bulk;
    store.set(key, value);

    List<RespValue> argValue = new ArrayList<>();
    argValue.add(new RespValue(RespType.BULK, "SET"));
    for (RespValue val : args) {
      argValue.add(val);
    }
    return new RespValue(argValue);
  }
}

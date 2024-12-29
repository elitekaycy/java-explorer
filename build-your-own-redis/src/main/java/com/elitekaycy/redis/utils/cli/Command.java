package utils.cli;

import utils.resp.RespValue;

/**
 * Interface representing a Redis command. This interface is implemented for various Redis
 * operations such as HGET, GET, SET, and HSET.
 *
 * <p>The implementing classes should provide the logic to execute the corresponding Redis command,
 * process the arguments, and return the appropriate result in the form of a {@link RespValue}.
 *
 * <p>For example:
 *
 * <ul>
 *   <li><b>GET</b>: Retrieves the value of a key.
 *   <li><b>SET</b>: Sets the value of a key.
 *   <li><b>HGET</b>: Retrieves the value of a field in a hash.
 *   <li><b>HSET</b>: Sets the value of a field in a hash.
 * </ul>
 *
 * <p>Each command will receive the necessary arguments (one or more {@link RespValue} objects),
 * perform the operation, and return the result encapsulated in a {@link RespValue}.
 *
 * <p>Example usage:
 *
 * <pre>
 *     Command getCommand = new GetCommand();
 *     RespValue result = getCommand.execute(new RespValue("key"));
 * </pre>
 *
 * @see RespValue
 */
public interface Command {
  /**
   * Executes the Redis command with the provided arguments and returns the result as a {@link
   * RespValue}.
   *
   * @param args The arguments for the command, represented as {@link RespValue} objects.
   * @return The result of executing the command, encapsulated in a {@link RespValue}.
   */
  RespValue execute(RespValue... args);
}

package utils.cli;

import utils.resp.RespType;
import utils.resp.RespValue;

public class PingCommand implements Command {

  @Override
  public RespValue execute(RespValue... args) {
    if (args.length == 0) {
      return new RespValue(RespType.SIMPLE, "PONG");
    }
    return new RespValue(RespType.BULK, args[0].bulk);
  }
}

package utils.cli;

import utils.resp.RespValue;

public interface Command {
  RespValue execute(RespValue... args);
}

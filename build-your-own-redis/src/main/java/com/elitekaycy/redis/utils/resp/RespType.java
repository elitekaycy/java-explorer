package utils.resp;

public enum RespType {
  SIMPLE('+'),
  ARRAY('*'),
  BULK('$'),
  AGGREGATE('/'),
  ERROR('-'),
  INT(':');

  private final char value;

  RespType(char value) {
    this.value = value;
  }

  public char getValue() {
    return value;
  }
}

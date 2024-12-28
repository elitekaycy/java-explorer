package utils.resp;

import java.util.List;

public class RespValue {

  public RespType typ;
  public String str;
  public Long num;
  public String bulk;
  public List<RespValue> array;

  public RespValue() {}

  public RespValue(RespType typ, String str) {
    if (typ == RespType.BULK) this.bulk = str;
    else this.str = str;

    this.typ = typ;
  }

  public RespValue(String str) {
    this.typ = RespType.SIMPLE;
    this.str = str;
  }

  public RespValue(Long num) {
    this.typ = RespType.INT;
    this.num = num;
  }

  public RespValue(List<RespValue> arr) {
    this.typ = RespType.ARRAY;
    this.array = arr;
  }

  @Override
  public String toString() {
    return "Value{"
        + "typ='"
        + typ
        + '\''
        + ", str='"
        + str
        + '\''
        + ", num="
        + num
        + ", bulk='"
        + bulk
        + '\''
        + ", array="
        + array
        + '}';
  }
}

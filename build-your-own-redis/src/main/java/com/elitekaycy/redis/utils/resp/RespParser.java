package utils.resp;

import java.util.ArrayList;
import java.util.List;

public class RespParser {

  private String data;
  private int pos;

  public RespParser(String data) {
    this.data = data;
  }

  public RespValue parse() {
    if (data == null || data.isEmpty()) return null;

    char type = data.charAt(pos++);

    switch (type) {
      case '+':
        return parseSimpleString();
      case '-':
        return parseError();
      case ':':
        return parseInteger();
      case '$':
        return parseBulkString();
      case '*':
        return parseArray();
      default:
        throw new RuntimeException("Unknown RESP type: " + type);
    }
  }

  private RespValue parseArray() {
    RespValue parser = new RespValue();
    int count = Integer.parseInt(readUntilCRLF());
    if (count == -1) return null;

    List<RespValue> result = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      result.add(parse());
    }

    parser.typ = RespType.ARRAY;
    parser.array = result;

    return parser;
  }

  private RespValue parseBulkString() {
    RespValue parser = new RespValue();
    int length = Integer.parseInt(readUntilCRLF());
    if (length == -1) return null;

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < length; i++) {
      result.append(data.charAt(pos++));
    }
    skipCRLF();

    parser.typ = RespType.BULK;
    parser.bulk = result.toString();
    return parser;
  }

  private String readUntilCRLF() {
    return parseSimple();
  }

  private RespValue parseInteger() {
    RespValue parseInt = new RespValue();

    parseInt.typ = RespType.INT;
    Long res = Long.parseLong(parseSimple());
    parseInt.num = res;
    return parseInt;
  }

  private RespValue parseError() {
    RespValue parserErr = new RespValue();
    String result = parseSimple();

    parserErr.typ = RespType.ERROR;
    parserErr.str = result;
    return parserErr;
  }

  private String parseSimple() {
    StringBuilder result = new StringBuilder();

    while (pos < data.length() && !isLineEnd()) {
      result.append(data.charAt(pos++));
    }

    skipCRLF();
    return result.toString();
  }

  private RespValue parseSimpleString() {
    RespValue parseStr = new RespValue();
    String result = parseSimple();
    parseStr.typ = RespType.SIMPLE;
    parseStr.str = result;
    return parseStr;
  }

  private void skipCRLF() {
    pos += 2;
  }

  private boolean isLineEnd() {
    return data.charAt(pos) == '\r';
  }

  // Testing the Parser here...
  public static void main(String[] args) {
    RespParser parser = new RespParser("+OK\r\n");
    Object result = parser.parse(); // Returns "OK"

    System.out.println("Resp parser parsee  " + result.toString());

    parser = new RespParser("$5\r\nhello\r\n");
    result = parser.parse(); // Returns "hello"
    //
    System.out.println("Resp parser parsee  " + result.toString());
  }
}

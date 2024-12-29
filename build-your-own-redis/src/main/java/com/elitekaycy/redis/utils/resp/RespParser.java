package utils.resp;

import java.util.ArrayList;
import java.util.List;

/**
 * A parser for RESP (REdis Serialization Protocol) format. Parses RESP formatted data into a
 * corresponding {@link RespValue}.
 */
public class RespParser {

  private String data;
  private int pos;

  /**
   * Constructs a new parser for the provided RESP data.
   *
   * @param data The RESP data to be parsed.
   */
  public RespParser(String data) {
    this.data = data;
  }

  /**
   * Parses the RESP data and returns a corresponding {@link RespValue}.
   *
   * @return A {@link RespValue} corresponding to the parsed data.
   * @throws RuntimeException If the RESP type is unknown.
   */
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

  /**
   * Parses a RESP array type.
   *
   * @return A {@link RespValue} representing the parsed array.
   */
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

  /**
   * Parses a RESP bulk string type.
   *
   * @return A {@link RespValue} representing the parsed bulk string.
   */
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

  /**
   * Reads data until a CRLF sequence is encountered.
   *
   * @return The parsed data as a string.
   */
  private String readUntilCRLF() {
    return parseSimple();
  }

  /**
   * Parses a RESP integer type.
   *
   * @return A {@link RespValue} representing the parsed integer.
   */
  private RespValue parseInteger() {
    RespValue parseInt = new RespValue();

    parseInt.typ = RespType.INT;
    Long res = Long.parseLong(parseSimple());
    parseInt.num = res;
    return parseInt;
  }

  /**
   * Parses a RESP error type.
   *
   * @return A {@link RespValue} representing the parsed error.
   */
  private RespValue parseError() {
    RespValue parserErr = new RespValue();
    String result = parseSimple();

    parserErr.typ = RespType.ERROR;
    parserErr.str = result;
    return parserErr;
  }

  /**
   * Parses a simple RESP string (e.g., simple strings, errors).
   *
   * @return The parsed simple string.
   */
  private String parseSimple() {
    StringBuilder result = new StringBuilder();

    while (pos < data.length() && !isLineEnd()) {
      result.append(data.charAt(pos++));
    }

    skipCRLF();
    return result.toString();
  }

  /**
   * Parses a RESP simple string type.
   *
   * @return A {@link RespValue} representing the parsed simple string.
   */
  private RespValue parseSimpleString() {
    RespValue parseStr = new RespValue();
    String result = parseSimple();
    parseStr.typ = RespType.SIMPLE;
    parseStr.str = result;
    return parseStr;
  }

  /** Skips the CRLF sequence in the data. */
  private void skipCRLF() {
    pos += 2;
  }

  /**
   * Checks if the current position is at the end of a line (i.e., the CR character).
   *
   * @return {@code true} if the current position is at the end of a line; {@code false} otherwise.
   */
  private boolean isLineEnd() {
    return data.charAt(pos) == '\r';
  }
}

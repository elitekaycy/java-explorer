package utils.resp;

import java.nio.ByteBuffer;

public class RespSerializer {
  private RespValue data;

  public RespSerializer(RespValue data) {
    this.data = data;
  }

  public byte[] serialize() {
    switch (data.typ) {
      case RespType.BULK:
        return serializeBulk();
      case RespType.ARRAY:
        return serializeArray();
      case RespType.SIMPLE:
        return serializeSimple();
      case RespType.ERROR:
        return serializeError();
      case RespType.INT:
        return serializeInteger();
      default:
        return new byte[] {};
    }
  }

  private byte[] serializeInteger() {
    ByteBuffer buffer = ByteBuffer.allocate(String.valueOf(data.num).length() + 3);

    buffer.put((byte) RespType.INT.getValue());
    buffer.put(String.valueOf(data.num).getBytes());
    buffer.put((byte) '\r');
    buffer.put((byte) '\n');

    return buffer.array();
  }

  private byte[] serializeError() {
    ByteBuffer buffer = ByteBuffer.allocate(data.str.length() + 3);
    buffer.put((byte) RespType.ERROR.getValue());
    buffer.put(data.str.getBytes());
    buffer.put((byte) '\r');
    buffer.put((byte) '\n');
    return buffer.array();
  }

  private byte[] serializeSimple() {
    ByteBuffer buffer = ByteBuffer.allocate(data.str.length() + 3);
    buffer.put((byte) RespType.SIMPLE.getValue());
    buffer.put(data.str.getBytes());
    buffer.put((byte) '\r');
    buffer.put((byte) '\n');

    System.out.println("serialize simple " + new String(buffer.array()));
    return buffer.array();
  }

  private byte[] serializeBulk() {
    ByteBuffer bufr =
        ByteBuffer.allocate(
            1 + String.valueOf(data.bulk.length()).length() + 2 + data.bulk.length() + 2);

    bufr.put((byte) RespType.BULK.getValue());
    bufr.put(String.valueOf(data.bulk.length()).getBytes());
    bufr.put("\r\n".getBytes());
    bufr.put(data.bulk.getBytes());
    bufr.put("\r\n".getBytes());

    return bufr.array();
  }

  private byte[] serializeArray() {
    int len = data.array.size();
    ByteBuffer buffer = ByteBuffer.allocate(1 + String.valueOf(len).length() + 2 + len * 10);

    buffer.put((byte) RespType.ARRAY.getValue());
    buffer.put(String.valueOf(len).getBytes());
    buffer.put("\r\n".getBytes());

    for (RespValue element : data.array) {
      byte[] marshaledElement = new RespSerializer(element).serialize();
      buffer.put(marshaledElement);
    }

    return buffer.array();
  }

  public static void main(String[] args) {
    RespSerializer test = new RespSerializer(new RespValue(RespType.SIMPLE, "Hello wordl hello"));

    System.out.println(new String(test.serialize()));
  }
}

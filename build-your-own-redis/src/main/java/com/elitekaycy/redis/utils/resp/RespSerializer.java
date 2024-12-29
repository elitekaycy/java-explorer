package utils.resp;

import java.nio.ByteBuffer;

/**
 * A singleton class responsible for serializing {@link RespValue} objects into RESP (REdis
 * Serialization Protocol) byte arrays.
 */
public class RespSerializer {

  private RespSerializer() {}

  private static class RespSerializerHolder {
    private static final RespSerializer INSTANCE = new RespSerializer();
  }

  /**
   * Returns the singleton instance of the {@link RespSerializer}.
   *
   * @return The single instance of the RespSerializer.
   */
  public static RespSerializer getInstance() {
    return RespSerializerHolder.INSTANCE;
  }

  /**
   * Serializes a {@link RespValue} object into its corresponding RESP byte array.
   *
   * @param data The {@link RespValue} to serialize.
   * @return A byte array representing the serialized RESP data.
   */
  public byte[] serialize(RespValue data) {
    switch (data.typ) {
      case RespType.BULK:
        return serializeBulk(data);
      case RespType.ARRAY:
        return serializeArray(data);
      case RespType.SIMPLE:
        return serializeSimple(data);
      case RespType.ERROR:
        return serializeError(data);
      case RespType.INT:
        return serializeInteger(data);
      default:
        return new byte[] {};
    }
  }

  /**
   * Serializes an integer type RESP value.
   *
   * @param data The {@link RespValue} of integer type.
   * @return A byte array representing the serialized integer value.
   */
  private byte[] serializeInteger(RespValue data) {
    ByteBuffer buffer = ByteBuffer.allocate(String.valueOf(data.num).length() + 3);
    buffer.put((byte) RespType.INT.getValue());
    buffer.put(String.valueOf(data.num).getBytes());
    buffer.put((byte) '\r');
    buffer.put((byte) '\n');
    return buffer.array();
  }

  /**
   * Serializes an error type RESP value.
   *
   * @param data The {@link RespValue} of error type.
   * @return A byte array representing the serialized error value.
   */
  private byte[] serializeError(RespValue data) {
    ByteBuffer buffer = ByteBuffer.allocate(data.str.length() + 3);
    buffer.put((byte) RespType.ERROR.getValue());
    buffer.put(data.str.getBytes());
    buffer.put((byte) '\r');
    buffer.put((byte) '\n');
    return buffer.array();
  }

  /**
   * Serializes a simple string type RESP value.
   *
   * @param data The {@link RespValue} of simple string type.
   * @return A byte array representing the serialized simple string value.
   */
  private byte[] serializeSimple(RespValue data) {
    ByteBuffer buffer = ByteBuffer.allocate(data.str.length() + 3);
    buffer.put((byte) RespType.SIMPLE.getValue());
    buffer.put(data.str.getBytes());
    buffer.put((byte) '\r');
    buffer.put((byte) '\n');
    return buffer.array();
  }

  /**
   * Serializes a bulk string type RESP value.
   *
   * @param data The {@link RespValue} of bulk string type.
   * @return A byte array representing the serialized bulk string value.
   */
  private byte[] serializeBulk(RespValue data) {
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

  /**
   * Serializes an array type RESP value.
   *
   * @param data The {@link RespValue} of array type.
   * @return A byte array representing the serialized array value.
   */
  private byte[] serializeArray(RespValue data) {
    int len = data.array.size();
    ByteBuffer buffer = ByteBuffer.allocate(1 + String.valueOf(len).length() + 2 + len * 10);
    buffer.put((byte) RespType.ARRAY.getValue());
    buffer.put(String.valueOf(len).getBytes());
    buffer.put("\r\n".getBytes());

    for (RespValue element : data.array) {
      byte[] marshaledElement = RespSerializer.getInstance().serialize(element);
      if (buffer.remaining() < marshaledElement.length) {
        buffer = resizeBuffer(buffer, marshaledElement.length);
      }
      buffer.put(marshaledElement);
    }

    return buffer.array();
  }

  /**
   * Resizes a {@link ByteBuffer} to accommodate additional data.
   *
   * @param oldBuffer The existing {@link ByteBuffer}.
   * @param requiredSpace The required space for the new data.
   * @return A new {@link ByteBuffer} with the resized capacity.
   */
  private ByteBuffer resizeBuffer(ByteBuffer oldBuffer, int requiredSpace) {
    int newSize = oldBuffer.capacity() + requiredSpace;
    ByteBuffer newBuffer = ByteBuffer.allocate(newSize);
    oldBuffer.flip();
    newBuffer.put(oldBuffer);
    return newBuffer;
  }
}

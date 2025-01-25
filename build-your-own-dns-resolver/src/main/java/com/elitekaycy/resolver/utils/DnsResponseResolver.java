package com.elitekaycy.resolver.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is to be able to read dns resolver packets coming back once the dns has been resolved
 */
public class DnsResponseResolver {

  public static DnsResponse parse(byte[] response) throws UnknownHostException {
    ByteBuffer buffer = ByteBuffer.wrap(response);

    DnsHeader header =
        new DnsHeader(
            buffer.getShort() & 0xFFFF,
            buffer.getShort() & 0xFFFF,
            buffer.getShort() & 0xFFFF,
            buffer.getShort() & 0xFFFF,
            buffer.getShort() & 0xFFFF,
            buffer.getShort() & 0xFFFF);

    List<DnsQuestion> questions = new ArrayList<>();
    for (int i = 0; i < header.questionCount(); i++) {
      String domainName = readDomainName(buffer);
      int type = buffer.getShort() & 0xFFFF;
      int clazz = buffer.getShort() & 0xFFFF;
      questions.add(new DnsQuestion(domainName, type, clazz));
    }

    List<DnsAnswer> answers = new ArrayList<>();
    for (int i = 0; i < header.answerCount(); i++) {
      String name = readDomainName(buffer);
      int type = buffer.getShort() & 0xFFFF;
      int clazz = buffer.getShort() & 0xFFFF;
      int ttl = buffer.getInt();
      int dataLength = buffer.getShort() & 0xFFFF;

      String data;
      if (type == 1) {
        byte[] ipAddressBytes = new byte[4];
        buffer.get(ipAddressBytes);
        InetAddress ipAddress = InetAddress.getByAddress(ipAddressBytes);
        data = ipAddress.getHostAddress();
      } else {
        byte[] rawData = new byte[dataLength];
        buffer.get(rawData);
        data = "Unsupported type";
      }

      answers.add(new DnsAnswer(name, type, clazz, ttl, data));
    }

    return new DnsResponse(header, questions, answers);
  }

  private static String readDomainName(ByteBuffer buffer) {
    StringBuilder domainName = new StringBuilder();
    while (true) {
      int length = buffer.get() & 0xFF;
      if (length == 0) {
        break;
      }
      // we only care that there is the first two bits 1s so we do a binary compare and make sure it
      // matches
      if ((length & 0xC0) == 0xC0) {
        int pointer = ((length & 0x3F) << 8) | (buffer.get() & 0xFF);
        ByteBuffer tempBuffer = buffer.duplicate();
        tempBuffer.position(pointer);
        domainName.append(readDomainName(tempBuffer));
        break;
      } else {
        byte[] label = new byte[length];
        buffer.get(label);
        domainName.append(new String(label)).append(".");
      }
    }
    if (domainName.length() > 0) {
      domainName.setLength(domainName.length() - 1); // Removing trailing dot
    }
    return domainName.toString();
  }

  public static record DnsResponse(
      DnsHeader header, List<DnsQuestion> questions, List<DnsAnswer> answers) {

    @Override
    public String toString() {
      return "DnsResponse{"
          + "header="
          + header
          + ", questions="
          + questions
          + ", answers="
          + answers
          + '}';
    }
  }

  public static record DnsHeader(
      int transactionId,
      int flags,
      int questionCount,
      int answerCount,
      int authorityCount,
      int additionalCount) {

    @Override
    public String toString() {
      return "DnsHeader{"
          + "transactionId="
          + transactionId
          + ", flags=0x"
          + Integer.toHexString(flags)
          + ", questionCount="
          + questionCount
          + ", answerCount="
          + answerCount
          + ", authorityCount="
          + authorityCount
          + ", additionalCount="
          + additionalCount
          + '}';
    }
  }

  public static record DnsQuestion(String domainName, int type, int clazz) {
    @Override
    public String toString() {
      return "DnsQuestion{"
          + "domainName='"
          + domainName
          + '\''
          + ", type="
          + type
          + ", clazz="
          + clazz
          + '}';
    }
  }

  public static record DnsAnswer(String name, int type, int clazz, int ttl, String data) {
    @Override
    public String toString() {
      return "DnsAnswer{"
          + "name='"
          + name
          + '\''
          + ", type="
          + type
          + ", clazz="
          + clazz
          + ", ttl="
          + ttl
          + ", data='"
          + data
          + '\''
          + '}';
    }
  }
}

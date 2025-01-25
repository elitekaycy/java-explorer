package com.elitekaycy.resolver.utils;

import java.nio.ByteBuffer;

/**
 * This Class is mainly to be able to build a message to be sent to the name server based on the RFC
 * 1035 standard
 */
public class DnsQuery {
  private final String domainName;
  private final short transactionId;

  public DnsQuery(String domainName, short transactionId) {
    this.domainName = domainName;
    this.transactionId = transactionId;
  }

  public byte[] buildQueryPackets() {
    ByteBuffer buffer = ByteBuffer.allocate(512);
    /**
     * A DNS message has:
     *
     * <p>A header. A questions section. An answer section. An authority section. An additional - I
     * would add them as bytes
     */
    buffer.putShort((byte) transactionId); // First is always the transactionId
    buffer.putShort((short) 0x0100); // Flags: standard query
    buffer.putShort((short) 1); // QDCOUNT: 1 question
    buffer.putShort((short) 0); // ANCOUNT
    buffer.putShort((short) 0); // NSCOUNT
    buffer.putShort((short) 0); // ARCOUNT

    // So i would add the domain name here as bytes but encoded with byte length
    for (String sub : domainName.split("\\.")) {
      buffer.put((byte) sub.length());
      buffer.put(sub.getBytes());
    }

    buffer.putShort((byte) 0); // This is the terminating byte for the domain

    buffer.putShort((short) 1); // QTYPE: A (host address)
    buffer.putShort((short) 1); // QCLASS: IN (Internet)

    buffer.flip();
    return buffer.array();
  }
}

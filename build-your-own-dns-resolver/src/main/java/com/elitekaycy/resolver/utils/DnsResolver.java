package com.elitkaycy.resolver.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import com.elitekaycy.resolver.utils.DnsQuery;

public class DnsResolver {
  private static final String DNS_SERVER = "8.8.8.8";
  private static final int DNS_PORT = 53;

  public byte[] sendDnsQuery(String domainName) {
    try {
     short transactionId = (short) new Random().nextInt(Short.MAX_VALUE);
     DnsQuery query = new DnsQuery(domainName, transactionId);
     byte[] queryPackets = query.buildQueryPackets();
     
     DatagramSocket socket = new DatagramSocket();
     InetAddress serverAddress = InetAddress.getByName(DNS_SERVER);

      DatagramPacket requestPacket = new DatagramPacket(queryPackets, queryPackets.length, serverAddress, DNS_PORT );

     socket.send(requestPacket);

     byte[] responseBuffer = new byte[512];
     DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);

   socket.receive(responsePacket);
   socket.close();
   return responsePacket.getData();
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    return null;
  }
}

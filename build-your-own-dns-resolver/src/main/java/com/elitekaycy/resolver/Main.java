package com.elitekaycy.resolver;

import java.net.UnknownHostException;

import com.elitekaycy.resolver.utils.DnsResponseResolver;
import com.elitekaycy.resolver.utils.DnsResponseResolver.DnsResponse;
import com.elitkaycy.resolver.utils.DnsResolver;

public class Main {

  public static void main(String[] args) throws UnknownHostException {

    DnsResolver resolver = new DnsResolver();
    String domainName = "www.yahoo.com";

    byte[] response = resolver.sendDnsQuery(domainName);
    DnsResponse parsedResponse = DnsResponseResolver.parse(response);
    System.out.println(parsedResponse);
  }
}

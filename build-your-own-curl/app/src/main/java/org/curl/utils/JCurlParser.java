package org.curl.utils;
import java.util.*;

public class JCurlParser {
    private final static String[] ignores = {"GET", "POST", "PUT", "PATCH", "\"", "{"};
    
    public static JCurlRequest parse(String... args) {
        if(args.length == 0) {
            System.out.println("Usage: jcurl [method] [options] <url>");
            return null;
        }
        
        JCurlRequest request = new JCurlRequest();
        
        for(int i = 0; i < args.length; i++) {
            String arg = args[i];
            if(arg.startsWith("-")) {
                switch (arg) {
                    case "-h": 
                        System.out.println("Usage: jcurl [method] [options] <url>");
                        return null;
                    case "-v": 
                        request.setVerbose(true);
                        break;  
                    case "-H":
                        if (i + 1 < args.length) {
                            parseHeader(request, args, i + 1);
                            i++;  
                        } else {
                            System.out.println("Error: Missing header argument after -H");
                        }
                        break;
                    case "-d":
                        if (i + 1 < args.length) {
                            parseData(request, args, i + 1);
                            i++;  
                        } else {
                            System.out.println("Error: Missing data argument after -d");
                        }
                        break;  
                    case "-X":
                    case "--request":
                        if (i + 1 < args.length) {
                            request.setMethod(args[i + 1]);
                            i++;  
                        } else {
                            System.out.println("Error: Missing method argument after -X or --request");
                        }
                        break;
                    default:
                        System.out.println("Warning: Unknown option: " + arg);
                        break;
                }
            } else if(!Arrays.asList(ignores).contains(arg)) {
                request.setUrl(arg);
            }
        }
        
        if(request.getMethod() == null || request.getMethod().isEmpty()) {
            request.setMethod("GET");
        }
        
        return request;
    }
    
    private static void parseData(JCurlRequest request, String[] args, int currIdx) {
        if (currIdx < args.length) {
            String data = args[currIdx];
            request.setBody(data);
        }
    }
    
        private static void parseHeader(JCurlRequest request, String[] args, int currIdx) {
        if (currIdx >= args.length) {
            return;
        }
        
        String headerArg = args[currIdx];
        if (headerArg.startsWith("\"") && headerArg.endsWith("\"")) {
            headerArg = headerArg.substring(1, headerArg.length() - 1);
        }
        
        String[] headers = headerArg.split(",");
        for (String header : headers) {
            int colonIndex = header.indexOf(':');
            if (colonIndex != -1) {
                String name = header.substring(0, colonIndex).trim();
                String value = header.substring(colonIndex + 1).trim();
                request.addHeader(name, value);
            } else {
                System.out.println("Error: Invalid header format. Expected 'name:value' but got: " + header);
            }
        }
    }
}

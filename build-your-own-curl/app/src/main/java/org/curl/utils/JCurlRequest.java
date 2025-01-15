package org.curl.utils;

import java.util.HashMap;
import java.util.*;

public class JCurlRequest {
    private String url;
    private String method;
    private boolean verbose;
    private String contentType;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public JCurlRequest(){}

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMethod(String method) {
       this.method = method;
    }

    public void getContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return this.url;
    }

    public String getMethod() {
        return this.method;
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getContentType() {
        return this.contentType;
    }


    public boolean isVerbose() { return verbose; };

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
    
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("JCurlRequest {").append("\n")
      .append("  URL: ").append(this.url != null ? this.url : "N/A").append("\n")
      .append("  Method: ").append(this.method != null ? this.method : "N/A").append("\n")
      .append("  Content-Type: ").append(this.contentType != null ? this.contentType : "N/A").append("\n")
      .append("  Verbose: ").append(this.verbose).append("\n")
      .append("  Body: ").append(this.body != null ? this.body : "N/A").append("\n")
      .append("  Headers: ").append(this.headers.isEmpty() ? "None" : "").append("\n");

    
    if (!this.headers.isEmpty()) {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            sb.append("    ").append(header.getKey()).append(": ").append(header.getValue()).append(",").append("\n");
        }
    }

    sb.append("}");
    return sb.toString();
}
}

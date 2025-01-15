package org.curl;

import org.curl.utils.JCurlCommandClient;

public class App {
    public static void main(String[] args) {
       new JCurlCommandClient().execute(args);
    }
}

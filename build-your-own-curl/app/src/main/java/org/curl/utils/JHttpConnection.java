package org.curl.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class JHttpConnection {
    JCurlRequest request;

    public JHttpConnection(JCurlRequest request) {
        this.request = request;
    }


    public void doGet() {
        HttpURLConnection connection = null;
        try {

            URL url =  new URI(request.getUrl()).toURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            for(Map.Entry<String, String> headers : request.getHeaders().entrySet()) {
                connection.setRequestProperty(headers.getKey(), headers.getValue());
            }

            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
               String line;
                while((line = in.readLine()) != null) {
                    System.out.println("> " + line);
                }
            } 
            else {
                System.out.println("> " + "failed to get request");
            }
        } catch (Exception ignored) {
        }

        finally {
            connection.disconnect();
        }
    }


    public void doPost() {
    HttpURLConnection connection = null;
    try { URL url = new URI(request.getUrl()).toURL(); connection = (HttpURLConnection) url.openConnection(); connection.setRequestMethod("POST");

        for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }

        connection.setDoOutput(true);

        String body = request.getBody();
        if (body != null && !body.isEmpty()) {
            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes("UTF-8"));
                os.flush();
            }
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("> " + line);
                }
            }
        } else {
            System.out.println("> Request failed with response code: " + responseCode);
        }
    } catch (Exception e) {
        System.err.println("> Error during POST request: " + e.getMessage());
    } finally {
        if (connection != null) {
            connection.disconnect();
        }
    }
}

    
}

package org.curl.utils;


public class JCurlCommandClient implements JCurlClient {

    public JCurlCommandClient(){}

    @Override
    public void execute(String[] args){   
        JCurlRequest request = JCurlParser.parse(args);
        System.out.println(request);
        JHttpConnection connection = new JHttpConnection(request);

        switch(request.getMethod()) {
            case "GET":
              connection.doGet();
              break;
            case "POST":
              connection.doPost();
              break;
            default:
               System.out.println("> request method not found!!");


        }

    }
    
}

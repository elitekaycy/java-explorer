# JCurl: A Custom Curl Utility in Java

The **JCurl utility** is a Java-based HTTP client designed to replicate some of the functionality of the popular `curl` command-line tool. It allows users to send HTTP requests (e.g., GET, POST) with customizable headers, content types, and other parameters. The utility also includes a shell script wrapper (`jcurl.sh`) to simplify interactions and supports execution via Gradle.

---

## Why This Structure?
The design of JCurl is based on the **Single Responsibility Principle** and clean architecture. Each class serves a specific purpose, ensuring modularity, maintainability, and extensibility. Here's why we have separate classes:

1. **JCurlParser**: Handles parsing and validation of command-line arguments, isolating input processing logic.
2. **JCurlRequest**: Encapsulates request-related data (URL, method, headers, body) in a single object, simplifying communication between components.
3. **JCurlCommandClient**: Manages the actual HTTP execution (GET, POST, etc.) and delegates tasks to appropriate methods based on the request type.
4. **JCurlResponse**: Handles server responses, logging success or error details clearly.

This structure allows for future extensions, such as adding support for new HTTP methods or enhancing logging, without disrupting existing functionality.

---

## Key Components

### 1. JCurlParser
Responsible for parsing command-line arguments and converting them into a `JCurlRequest` object.

#### Code Example:
```java
import java.util.HashMap;
import java.util.Map;

public class JCurlParser {

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
                        ...
}
```

---

### 2. JCurlRequest
Encapsulates all the details of the HTTP request.

#### Code Example:
```java
import java.util.Map;

public class JCurlRequest {
    private String url;
    private String method;
    private Map<String, String> headers;
    private String body;
}
```

---

### 3. JCurlCommandClient
Handles the logic for executing HTTP requests based on the method specified in the `JCurlRequest` object.

#### Code Example:
```java
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JCurlCommandClient {

    public void execute(JCurlRequest request) throws Exception {
        switch (request.getMethod().toUpperCase()) {
            case "GET":
                doGet(request);
                break;
            case "POST":
                doPost(request);
                break;
                  ...
        }
    }

    private void doGet(JCurlRequest request) throws Exception {
       ...
    }

    private void doPost(JCurlRequest request) throws Exception {
        ...
    }
}
```

---

### 4. JCurlResponse
Handles logging and processing of the HTTP response.

#### Code Example:
```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class JCurlResponse {

    public static void handleResponse(HttpURLConnection connection, int responseCode) throws Exception {
        System.out.println("Response Code: " + responseCode);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                connection.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.err.println("Error reading response: " + e.getMessage());
        }
    }
}
```

---

## How to Run

### Using Gradle
Run the utility via Gradle with the `-Pargs` option to pass arguments:

```bash
./gradlew cmdLineJavaExec -Pargs="-X GET http://www.google.com"
```

### Using Shell Script
Run the utility with the `jcurl.sh` script:

```bash
./jcurl.sh -X GET http://www.google.com
```

---

## Examples

### Sending a GET Request
```bash
# Gradle
./gradlew cmdLineJavaExec -Pargs="-X GET http://www.google.com"

# Shell Script
./jcurl.sh -X GET http://www.google.com
```

### Sending a POST Request
```bash
# Gradle
./gradlew cmdLineJavaExec -Pargs="-X POST http://www.example.com -H \"Content-Type:application/json\" -d \"{\\"key\\":\\"value\\"}\""

# Shell Script
./jcurl.sh -X POST http://www.example.com -H "Content-Type:application/json" -d '{"key":"value"}'
```

![sample](./docs/curl.png)

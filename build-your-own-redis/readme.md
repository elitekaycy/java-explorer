# Redis-like Java Server Implementation

This project demonstrates a simple Redis-like server built in Java. The server listens for client connections over a socket, processes commands using the RESP (Redis Serialization Protocol), and responds accordingly. We implement basic Redis commands like `PING`, `SET`, and `GET`, and show how to handle the parsing of client input, command handling, and data serialization.

## Overview of Redis

Redis is an open-source, in-memory data structure store that supports various data types, including strings, hashes, lists, sets, and more. It communicates with clients over the **RESP** (Redis Serialization Protocol), which defines how to format commands and responses.

- **RESP**: A binary-safe serialization protocol used by Redis for communication. It includes types like:
  - **Simple Strings**: Represented as strings and prefixed with `+`.
  - **Bulk Strings**: Represented by `$` and used for data like strings or binary data.
  - **Arrays**: Represented by `*`, which can contain multiple RESP types.
  - **Integers**: Represented by `:`, used for numeric data.
  
In this implementation, we handle the **bulk strings** and **arrays** for client commands.

## How the Server Works

### Step-by-Step Workflow:
1. **Receive Input**: The server waits for client connections over a socket. Once a connection is made, it listens for commands sent by the client.
2. **Parse Input**: The input is read as a line from the client and split into components: the command name and its arguments. The arguments are then converted into `RespValue` objects.
3. **Command Handling**: The server looks up the command handler based on the command name (e.g., `PING`, `SET`). The appropriate handler function is invoked to process the command.
4. **Save Data**: For commands like `SET`, data is saved in memory. For commands like `GET`, the server retrieves the data associated with the key.
5. **Return Data**: The server returns the result back to the client, formatted according to the RESP protocol.

### Sample Commands Implemented:
- **PING**: Responds with `PONG`.
- **SET**: Sets a key-value pair in memory.
- **GET**: Retrieves the value for a given key.

## Code Breakdown

### 1. **Server Setup**

The server listens on port `6379` and waits for incoming client connections. For each connection, it spawns a new thread to handle the client request:

```java
public class Main {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(6379)) {
            System.out.println("Server socket started on " + 6379);

            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new ClientHandler(socket, new CommandHandlerMapper()));
                thread.start();
            }

        } catch (IOException ignored) {
        }
    }
}



# Running the Redis-like Server

To run the Redis-like server, follow these steps:

### 1. **Compile the Code**:

First, navigate to the project directory and compile the Java code. Use the following command to compile your Java classes:

```bash
javac -d out src/*.java
```

### 2. **Run the Server**

After successfully compiling the code run the server with the ff commands

```bash
java -cp out Main
```


# Connecting to the Server Using Telnet

## Step 3: Connect to the Server
```bash
telnet localhost 6379
```


### Step 4: Test the Commands

- Ping Command
```
PING
```

```
SET key value
```

```
GET key
```

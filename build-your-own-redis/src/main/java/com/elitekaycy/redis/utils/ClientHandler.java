package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import utils.cli.Command;
import utils.cli.CommandHandlerMapper;
import utils.resp.RespType;
import utils.resp.RespValue;
import utils.store.impl.AofStore;

public class ClientHandler implements Runnable {

  private Socket socket;
  private CommandHandlerMapper handler;
  private AofStore aofStore;

  public ClientHandler(Socket socket, CommandHandlerMapper handler, AofStore aofStore) {
    this.socket = socket;
    this.handler = handler;
    this.aofStore = aofStore;
  }

  @Override
  public void run() {

    try (InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        BufferedReader reader = new BufferedReader((new InputStreamReader(inputStream)));
        PrintWriter writer = new PrintWriter(outputStream, true); ) {

      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println("Received: " + line);

        String[] parts = line.split(" ");
        String commandName = parts[0];
        RespValue[] args = parseArgs(Arrays.copyOfRange(parts, 1, parts.length));

        Command handler = this.handler.getHandler(commandName);
        if (handler != null) {
          RespValue response = handler.execute(args);
          if (commandName.equals("SET") || commandName.equals("HSET")) {
            aofStore.write(response);
          }
          writer.println(response);
        } else {
          writer.println("ERROR: Unknown command");
        }
      }

    } catch (IOException e) {
      System.out.println("Error handling client " + e.getMessage());
    }
  }

  private RespValue[] parseArgs(String[] args) {
    RespValue[] values = new RespValue[args.length];
    for (int i = 0; i < args.length; i++) {
      values[i] = new RespValue(RespType.BULK, args[i]);
    }
    return values;
  }
}

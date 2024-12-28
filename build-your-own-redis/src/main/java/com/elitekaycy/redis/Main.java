import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import utils.ClientHandler;
import utils.cli.CommandHandlerMapper;

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

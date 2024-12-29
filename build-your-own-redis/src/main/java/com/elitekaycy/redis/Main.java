import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import utils.ClientHandler;
import utils.cli.CommandHandlerMapper;
import utils.store.impl.AofStore;

public class Main {

  public static void main(String[] args) {
    AofStore aofStore = new AofStore();

    try (ServerSocket serverSocket = new ServerSocket(6379)) {
      System.out.println("Server socket started on " + 6379);

      aofStore.read();
      AofStore.sync();

      while (true) {
        Socket socket = serverSocket.accept();

        Thread thread = new Thread(new ClientHandler(socket, new CommandHandlerMapper(), aofStore));
        thread.start();
      }

    } catch (IOException ignored) {
    }
  }
}

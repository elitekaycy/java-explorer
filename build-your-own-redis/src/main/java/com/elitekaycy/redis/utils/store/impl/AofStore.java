package utils.store.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import utils.resp.RespParser;
import utils.resp.RespSerializer;
import utils.resp.RespValue;
import utils.store.RedisStore;

/**
 * The AofStore class is responsible for writing and reading the Append-Only File (AOF) for a
 * Redis-like database. It supports writing RESP (Redis Serialization Protocol) formatted data and
 * parsing it to execute commands like SET and HSET on a RedisStore.
 */
public class AofStore {

  private final RedisStore store = RedisStoreImpl.getInstance();
  private final Lock lock = new ReentrantLock();
  private static final String FILE_PATH = "database.aof";

  static {
    try (RandomAccessFile file = new RandomAccessFile(FILE_PATH, "rw")) {
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Synchronizes the AOF file by forcing its content to disk periodically. This operation is
   * performed in a separate thread to prevent blocking.
   */
  public static void sync() {
    new Thread(
            () -> {
              try (RandomAccessFile file = new RandomAccessFile(FILE_PATH, "rw")) {
        System.out.println("syncing...");
                // Forces the changes to be written to the disk.
                file.getChannel().force(true);
              } catch (IOException e) {
                e.printStackTrace();
              }

              // Waits for 2 seconds before performing the next synchronization.
              try {
                Thread.sleep(2000);
              } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
              }
            })
        .start();
  }

  /**
   * Writes one or more RESP values to the AOF file.
   *
   * @param values The RESP values to write to the file.
   */
  public void write(RespValue... values) {
    lock.lock();
    try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
      StringBuilder sb = new StringBuilder();

      for (RespValue value : values) {
        sb.append(new String(RespSerializer.getInstance().serialize(value)));
      }

      writer.write(sb.toString());
      writer.write(System.lineSeparator());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }

  /**
   * Reads the AOF file line by line, parses RESP data, and handles each RESP value. The handling is
   * done based on the command in the RESP value.
   */
  public void read() {
    lock.lock();
    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
      StringBuilder sb = new StringBuilder();
      String line;

      // Reads each line from the file and processes commands.
      while ((line = reader.readLine()) != null) {
        if (line.isEmpty()) {
          if (sb.length() > 0) {
            RespValue value = new RespParser(sb.toString()).parse();
            handleRespValue(value);
            sb.setLength(0);
          }
        } else {
          sb.append(line).append("\r\n");
        }
      }

      if (sb.length() > 0) {
        RespValue value = new RespParser(sb.toString()).parse();
        handleRespValue(value);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }

  /**
   * Handles RESP values by parsing commands and executing corresponding methods on the RedisStore.
   *
   * @param value The parsed RESP value containing the command and arguments.
   */
  private void handleRespValue(RespValue value) {
    String command = value.array.get(0).bulk.toUpperCase();
    String[] args = value.array.stream().map(v -> v.bulk).toArray(String[]::new);

    switch (command) {
      case "SET":
        store.set(args[1], args[2]);
        break;
      case "HSET":
        store.hset(args[1], args[2], args[3]);
        break;
      default:
        System.out.println("Unsupported command in AOF: " + command);
    }
  }
}

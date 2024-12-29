import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Main {

  private static int countLines(List<String> lines) {
    return lines.size();
  }

  private static int countCharacters(List<String> lines) {
    int count = 0;
    for (String line : lines) {
      String[] words = line.split("\\s+");
      for (String word : words) {
        count += word.length();
      }
    }
    return count;
  }

  private static int countWords(List<String> lines) {
    int count = 0;
    for (String line : lines) {
      count += line.split("\\s+").length;
    }
    return count;
  }

  private static int countBytes(List<String> lines) {
    int byteCount = 0;
    for (String line : lines) {
      byteCount += line.getBytes(StandardCharsets.UTF_8).length;
      byteCount += System.lineSeparator().getBytes(StandardCharsets.UTF_8).length;
    }
    return byteCount;
  }

  public static void main(String... args) {
    if (args.length <= 1) {
      System.out.println("USAGE: java Main <file_path>");
      return;
    }

    StringBuilder result = new StringBuilder();
    HashSet<String> commands = new HashSet<>();
    String filePath = (String) args[args.length - 1];
    String[] copyArgs = Arrays.copyOf(args, args.length - 1);

    List<String> lines = new ArrayList<>();
    try {
      lines = Files.readAllLines(Paths.get(filePath));
    } catch (IOException ignored) {
    }

    for (String arg : copyArgs) {
      if (commands.contains(arg)) {
        System.out.println("cannot have the same argument twice");
        return;
      }

      if (arg.equals("-l")) {
        result.append(countLines(lines)).append(" ");
      }

      if (arg.equals("-m")) {
        result.append(countCharacters(lines)).append(" ");
      }

      if (arg.equals("-w")) {
        result.append(countWords(lines)).append(" ");
      }

      if (arg.equals("-o")) {
        result.append(countBytes(lines)).append(" ");
      }

      commands.add(arg);
    }

    result.append((String) filePath);
    System.out.println(result.toString());
  }
}

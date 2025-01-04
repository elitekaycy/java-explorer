import java.util.HashMap;
import java.util.Map;
import utils.HuffmanNode;
import utils.HuffmanTree;
import utils.HuffmanUtil;

public class HuffmanMain {

  static String getEncodedOutput(String[] args) {
    if (args.length > 1) {
      return args[1];
    } else {
      String inputFilePath = args[0];
      if (inputFilePath.endsWith(".txt")) {
        return inputFilePath.substring(0, inputFilePath.lastIndexOf(".txt")) + ".encoded";
      } else {
        return inputFilePath + ".encoded";
      }
    }
  }

  public static void main(String[] args) {

    if (args.length < 1) {
      System.err.println("Usage: java HuffmanMain <file-path>");
      return;
    }

    String filePath = args[0];
    String outputFilePath = getEncodedOutput(args);

    try {
      Map<Character, Integer> freqMap = HuffmanUtil.getFrequencyMap(filePath);

      // Step 2: Build the Huffman Tree
      HuffmanNode root = HuffmanTree.buildTree(freqMap);

      // Step 3: Generate Huffman codes
      Map<Character, String> codeMap = new HashMap<>();
      HuffmanTree.generateCodes(root, "", codeMap);

      // Step 4: Encode and decode the input string
      String encodedString = HuffmanTree.encode(filePath, codeMap);
      // System.out.println("Encoded String: " + encodedString);

      HuffmanTree.writeEncodedFile(outputFilePath, freqMap, encodedString);

      /**
       * String decodedString = HuffmanTree.decode(encodedString, root); System.out.println("Decoded
       * String: " + decodedString);
       *
       * <p>System.out.println("Huffman Codes: " + codeMap);
       */
    } catch (Exception e) {
      System.err.println("Error processing file: " + e.getMessage());
      e.printStackTrace();
    }
  }
}

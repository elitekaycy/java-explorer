package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTree {

  public static HuffmanNode buildTree(Map<Character, Integer> freqMap) {
    PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();

    for (Map.Entry<Character, Integer> fm : freqMap.entrySet()) {
      pq.add(new HuffmanNode(fm.getKey(), fm.getValue()));
    }

    while (pq.size() > 1) {

      HuffmanNode left = pq.poll();
      HuffmanNode right = pq.poll();
      HuffmanNode internalNode = new HuffmanNode(left.frequency + right.frequency, left, right);
      pq.add(internalNode);
    }

    return pq.poll();
  }

  public static void generateCodes(HuffmanNode root, String code, Map<Character, String> codeMap) {
    if (root == null) return;

    if (root.left == null && root.right == null) {
      codeMap.put(root.character, code);
    } else {
      generateCodes(root.left, code + "0", codeMap);
      generateCodes(root.right, code + "1", codeMap);
    }
  }

  public static String encode(String input, Map<Character, String> codeMap) {
    String txt = "";
    try {

      txt = new String(Files.readAllBytes(Paths.get(input)));
    } catch (IOException ignored) {
    }
    StringBuilder result = new StringBuilder();
    for (char inp : txt.toCharArray()) {
      result.append(codeMap.get(inp));
    }
    return result.toString();
  }

  public static String decode(String encodedString, HuffmanNode root) {
    StringBuilder decodedString = new StringBuilder();
    HuffmanNode currentNode = root;

    for (char bit : encodedString.toCharArray()) {
      currentNode = (bit == '0') ? currentNode.left : currentNode.right;

      if (currentNode.left == null && currentNode.right == null) {
        decodedString.append(currentNode.character);
        currentNode = root;
      }
    }

    return decodedString.toString();
  }

  public static void writeEncodedFile(
      String filePath, Map<Character, Integer> freqMap, String encodedString) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
      oos.writeObject(freqMap);
      oos.writeUTF(encodedString);
      System.out.println("Encoded file created at: " + filePath);
    } catch (IOException ignored) {
    }
  }

  public static String decodeFromFile(String filePath) {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
      Map<Character, Integer> freqMap = (Map<Character, Integer>) ois.readObject();
      String encodedString = ois.readUTF();

      HuffmanNode root = HuffmanTree.buildTree(freqMap);

      return (String) HuffmanTree.decode(encodedString, root);
    } catch (IOException | ClassNotFoundException ignored) {
    }
    return null;
  }
}

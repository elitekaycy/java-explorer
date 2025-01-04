package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HuffmanUtil {
  public static Map<Character, Integer> getFrequencyMap(String file) {
    Map<Character, Integer> freqMap = new HashMap<>();

    try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = bf.readLine()) != null) {
        char[] characters = line.toCharArray();
        for (char ch : characters) {
          freqMap.put(ch, freqMap.getOrDefault(ch, 0) + 1);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return freqMap;
  }

  public static void readFrequencyMap(Map<Character, Integer> freqMap) {
    for (Map.Entry<Character, Integer> mp : freqMap.entrySet()) {
      System.out.println(mp.getKey() + " ==> " + mp.getValue());
    }
  }
}

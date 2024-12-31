package com.elitekaycy.json;

import java.nio.file.Files;
import java.nio.file.Path;

public class JsonFileReader {

  public static String read(String filePath) {
    try {
      Path path = Path.of(ClassLoader.getSystemResource(filePath).toURI());
      return Files.readString(path);
    } catch (Exception e) {
      throw new RuntimeException("Failed to read the file: " + filePath, e);
    }
  }

  public static void main(String[] args) {
    System.out.println(JsonFileReader.read("fail1.json"));
  }
}

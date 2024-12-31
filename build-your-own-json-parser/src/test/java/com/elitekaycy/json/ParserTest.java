package com.elitekaycy.json;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import com.elitekaycy.json.lexer.Lexer;
import com.elitekaycy.json.lexer.TokenType;
import com.elitekaycy.json.parser.Parser;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class ParserTest {
  @Test
  public void test_read_json() {
    String fail1 = JsonFileReader.read("./files/fail1.json");

    assertNotNull(fail1);
  }

  @Test
  public void test_simple_json() {

    String sm =
        "{\"string\":\"Hello,"
            + " World!\",\"number\":42,\"integer\":100,\"floating_point\":3.14159,\"boolean_true\":true,\"boolean_false\":false,\"null_value\":null,\"array\":[1,2,3,4,5],\"object\":{\"key1\":\"value1\",\"key2\":123},\"nested_array\":[{\"name\":\"John\",\"age\":30},{\"name\":\"Jane\",\"age\":25}],\"date\":\"2024-12-30T12:00:00Z\"}";

    List<TokenType<?>> lexer = new Lexer(sm).tokenize();
    assertNotNull(lexer);
  }

  @Test
  public void testFailJson() {

    String jsonContent = JsonFileReader.read("./files/fail1.json");

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new Parser(new Lexer(jsonContent).tokenize()).parse();
        });
  }

  @ParameterizedTest
  @MethodSource("provideFilePaths")
  public void testFailJson(String filePath) {
    System.out.println("filePath" + filePath);
    String jsonContent = JsonFileReader.read(filePath);

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new Parser(new Lexer(jsonContent).tokenize()).parse();
        });
  }

  static Stream<String> provideFilePaths() {
    return IntStream.rangeClosed(1, 33).mapToObj(i -> "./files/fail" + i + ".json");
  }
}

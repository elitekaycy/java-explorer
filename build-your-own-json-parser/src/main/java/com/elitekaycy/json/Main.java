package com.elitekaycy.json;

import com.elitekaycy.json.lexer.Lexer;
import com.elitekaycy.json.lexer.TokenType;
import com.elitekaycy.json.parser.Parser;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    String adv =
        "{\"string\":\"Hello,"
            + " World!\",\"number\":42,\"integer\":100,\"floating_point\":3.14159,\"boolean_true\":true,\"boolean_false\":false,\"null_value\":null,\"array\":[1,2,3,4,5],\"object\":{\"key1\":\"value1\",\"key2\":123},\"nested_array\":[{\"name\":\"John\",\"age\":30},{\"name\":\"Jane\",\"age\":25}],\"date\":\"2024-12-30T12:00:00Z\"}";

    Lexer lexer = new Lexer(adv);
    List<TokenType<?>> tokens = lexer.tokenize();
    Parser parser = new Parser(tokens);

    Object parsed = parser.parse();

    System.out.println(parsed);
  }
}

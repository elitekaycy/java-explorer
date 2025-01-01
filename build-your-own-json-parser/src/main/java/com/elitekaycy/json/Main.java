package com.elitekaycy.json;

import com.elitekaycy.json.lexer.Lexer;
import com.elitekaycy.json.lexer.TokenType;
import com.elitekaycy.json.model.JsonObject;
import com.elitekaycy.json.parser.Parser;
import com.elitekaycy.json.test.JsonTest;
import com.elitekaycy.json.util.JavaToJsonMapper;
import java.util.List;

public class Main {

  static class Person {
    private final String name;
    private final int age;

    public Person(String name, int age) {
      this.name = name;
      this.age = age;
    }
  }

  public static void main(String[] args) {
    String adv =
        "{\"string\":\"Hello,"
            + " World!\",\"number\":42,\"integer\":100,\"floating_point\":3.14159,\"boolean_true\":true,\"boolean_false\":false,\"null_value\":null,\"array\":[1,2,3,4,5],\"object\":{\"key1\":\"value1\",\"key2\":123},\"nested_array\":[{\"name\":\"John\",\"age\":30},{\"name\":\"Jane\",\"age\":25}],\"date\":\"2024-12-30T12:00:00Z\"}";

    Lexer lexer = new Lexer(adv);
    List<TokenType<?>> tokens = lexer.tokenize();
    Parser parser = new Parser(tokens);
    Object parsed = parser.parse();

    System.out.println(parsed);

    // Testing mapping a java object to a json string
    Person testPerson = new Person("dickson", 23);

    /**
     * Here I try to parse a Person Object to json string 
     */
    String jsonify = JavaToJsonMapper.toJson(testPerson);

    /*
     * Here i try to parse a Person Object to json Object 
     */
    JsonObject parsify = (JsonObject) JavaToJsonMapper.parse(testPerson);

    System.out.println(jsonify);
    System.out.println(parsify.toString());

    JsonTest.test();
  }
}

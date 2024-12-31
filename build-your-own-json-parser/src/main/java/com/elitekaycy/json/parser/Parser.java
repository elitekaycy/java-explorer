package com.elitekaycy.json.parser;

import com.elitekaycy.json.lexer.Token;
import com.elitekaycy.json.lexer.TokenType;
import com.elitekaycy.json.model.JsonArray;
import com.elitekaycy.json.model.JsonBoolean;
import com.elitekaycy.json.model.JsonKeyValue;
import com.elitekaycy.json.model.JsonNull;
import com.elitekaycy.json.model.JsonNumber;
import com.elitekaycy.json.model.JsonObject;
import com.elitekaycy.json.model.JsonString;
import java.util.List;

public class Parser {

  private final List<TokenType<?>> tokens;
  int pos;

  public Parser(List<TokenType<?>> tokens) {
    this.tokens = tokens;
    pos = 0;
  }

  public Object parse() {

    TokenType<?> token = tokens.get(pos);

    if (token.getToken() == Token.BEGIN_OBJECT) return parseObject();
    else if (token.getToken() == Token.BEGIN_ARRAY) return parseArray();
    else throw new IllegalArgumentException("Unexpected Token " + token);
  }

  private Object parseArray() {
    JsonArray jsonArray = new JsonArray();
    pos++;

    while (pos < tokens.size() && tokens.get(pos).getToken() != Token.END_ARRAY) {
      Object element = parseValue();
      jsonArray.addElement(element);

      if (pos < tokens.size() && tokens.get(pos).getToken() == Token.VALUE_SEPARATOR) {
        pos++;
      }
    }

    pos++;
    return jsonArray;
  }

  private Object parseValue() {
    TokenType<?> token = tokens.get(pos);

    switch (token.getToken()) {
      case STRING:
        pos++;
        return new JsonString((String) token.getValue());

      case NUMBER:
        pos++;
        return new JsonNumber((Number) token.getValue());

      case BOOLEAN:
        pos++;
        return new JsonBoolean((Boolean) token.getValue());

      case NULL:
        pos++;
        return new JsonNull(null);

      case BEGIN_OBJECT:
        return parseObject();

      case BEGIN_ARRAY:
        return parseArray();

      default:
        throw new IllegalArgumentException("Unexpected token: " + token);
    }
  }

  private JsonObject parseObject() {
    JsonObject jsonObject = new JsonObject();
    pos++;

    while (pos < tokens.size() && tokens.get(pos).getToken() != Token.END_OBJECT) {
      TokenType<?> token = tokens.get(pos);

      if (token.getToken() != Token.STRING) {
        throw new IllegalArgumentException("invalid token not string");
      }
      String key = (String) token.getValue();
      pos++;

      TokenType<?> column = tokens.get(pos);
      if (column.getToken() != Token.NAME_SEPARATOR)
        throw new IllegalArgumentException("invalid json -> needs to be a column");
      pos++;

      Object value = parseValue();
      jsonObject.add(new JsonKeyValue(key, value));

      TokenType<?> commaToken = tokens.get(pos);
      if (commaToken.getToken() == Token.VALUE_SEPARATOR) {
        pos++;
      }
    }

    return jsonObject;
  }
}

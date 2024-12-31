package com.elitekaycy.json.lexer;

public enum Token {
  BEGIN_ARRAY("["),
  END_ARRAY("]"),
  BEGIN_OBJECT("{"),
  END_OBJECT("}"),
  NAME_SEPARATOR(":"),
  VALUE_SEPARATOR(","),
  STRING("\""),
  NUMBER("-?(0|[1-9]\\d*)(\\.\\d+)?([eE][+-]?\\d+)?"),
  BOOLEAN("true|false"),
  NULL("null");

  private final String token;

  Token(String token) {
    this.token = token;
  }

  public String getValue() {
    return token;
  }

  public static Token fromChar(char c) {
    switch (c) {
      case '{':
        return BEGIN_OBJECT;
      case '}':
        return END_OBJECT;
      case '[':
        return BEGIN_ARRAY;
      case ']':
        return END_ARRAY;
      case ':':
        return NAME_SEPARATOR;
      case ',':
        return VALUE_SEPARATOR;
      case '"':
        return STRING;
      default:
        return NULL;
    }
  }
}

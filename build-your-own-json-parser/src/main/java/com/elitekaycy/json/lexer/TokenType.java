package com.elitekaycy.json.lexer;

public class TokenType<T> {
  private final Token token;
  private final T value;

  public TokenType(Token token, T value) {
    this.token = token;
    this.value = value;
  }

  public T getValue() {
    return this.value;
  }

  public Token getToken() {
    return token;
  }

  @Override
  public String toString() {
    return token + (value != null ? "(" + value + ")" : "");
  }
}

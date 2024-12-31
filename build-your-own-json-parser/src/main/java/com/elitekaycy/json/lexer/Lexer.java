package com.elitekaycy.json.lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

  private final String input;
  private int pos;

  public Lexer(String input) {
    this.input = input;
    this.pos = 0;
  }

  public List<TokenType<?>> tokenize() {
    ArrayList<TokenType<?>> tokens = new ArrayList<>();

    while (pos < input.length()) {
      char current = input.charAt(pos);
      Token token = Token.fromChar(input.charAt(pos));

      switch (token) {
        case BEGIN_OBJECT:
          tokens.add(new TokenType<>(Token.BEGIN_OBJECT, Token.BEGIN_OBJECT.getValue()));
          pos++;
          break;
        case BEGIN_ARRAY:
          tokens.add(new TokenType<>(Token.BEGIN_ARRAY, Token.BEGIN_ARRAY.getValue()));
          pos++;
          break;
        case END_ARRAY:
          tokens.add(new TokenType<>(Token.END_ARRAY, Token.END_ARRAY.getValue()));
          pos++;
          break;
        case END_OBJECT:
          tokens.add(new TokenType<>(Token.END_OBJECT, Token.END_OBJECT.getValue()));
          pos++;
          break;
        case NAME_SEPARATOR:
          tokens.add(new TokenType<>(Token.NAME_SEPARATOR, Token.NAME_SEPARATOR.getValue()));
          pos++;
          break;
        case VALUE_SEPARATOR:
          tokens.add(new TokenType<>(Token.VALUE_SEPARATOR, Token.VALUE_SEPARATOR.getValue()));
          pos++;
          break;
        case STRING:
          tokens.add(new TokenType<>(Token.STRING, tokenizeString()));
          break;
        default:
          if (Character.isWhitespace(current)) pos++;
          else if (Character.isDigit(current))
            tokens.add(new TokenType<Number>(Token.NUMBER, tokenizeInteger()));
          else tokenizeLiterals(current, tokens);
          break;
      }
    }

    return tokens;
  }

  private void tokenizeLiterals(char current, List<TokenType<?>> tokens) {
    if (current == 't' || current == 'f' || current == 'n') {
      if (current == 't' && input.startsWith("true", pos)) {
        tokens.add(new TokenType<Boolean>(Token.BOOLEAN, true));
        pos += 4;
      } else if (current == 'f' && input.startsWith("false", pos)) {
        tokens.add(new TokenType<Boolean>(Token.BOOLEAN, false));
        pos += 5;
      } else if (current == 'n' && input.startsWith("null", pos)) {
        tokens.add(new TokenType<Object>(Token.NULL, null));
        pos += 4;
      } else throw new IllegalArgumentException("Unexpected literal at position " + pos);
    } else {
      throw new IllegalArgumentException("Unexpected character: " + current);
    }
  }

  private Number tokenizeInteger() {
    int start = pos;

    while (pos < input.length()
        && (Character.isDigit(input.charAt(pos))
            || input.charAt(pos) == '.'
            || input.charAt(pos) == 'e'
            || input.charAt(pos) == 'E'
            || input.charAt(pos) == '-'
            || input.charAt(pos) == '+')) {
      pos++;
    }
    String numberStr = input.substring(start, pos);

    try {
      return Integer.parseInt(numberStr);
    } catch (NumberFormatException e) {
      try {
        return Double.parseDouble(numberStr);
      } catch (NumberFormatException ex) {
        throw new IllegalArgumentException("Invalid number format: " + numberStr);
      }
    }
  }

  private String tokenizeString() {
    pos++;
    StringBuilder sb = new StringBuilder();

    while (pos < input.length() && input.charAt(pos) != '"') {
      char current = input.charAt(pos);

      if (current == '\\') {
        pos++;
        if (pos < input.length()) {
          char escaped = input.charAt(pos);
          switch (escaped) {
            case '"':
              sb.append('"');
              break;
            case '\\':
              sb.append('\\');
              break;
            case '/':
              sb.append('/');
              break;
            case 'b':
              sb.append('\b');
              break;
            case 'f':
              sb.append('\f');
              break;
            case 'n':
              sb.append('\n');
              break;
            case 'r':
              sb.append('\r');
              break;
            case 't':
              sb.append('\t');
              break;
            default:
              throw new IllegalArgumentException("Invalid escape sequence: \\" + escaped);
          }
        }
      } else {
        sb.append(current);
      }

      pos++;
    }

    pos++;
    return sb.toString();
  }
}

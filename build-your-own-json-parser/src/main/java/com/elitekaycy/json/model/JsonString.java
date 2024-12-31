package com.elitekaycy.json.model;

public class JsonString extends JsonValue {
  private final String value;

  public JsonString(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "\"" + value + "\"";
  }
}

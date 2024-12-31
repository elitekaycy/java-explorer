package com.elitekaycy.json.model;

public class JsonNull extends JsonValue {
  private final Object value;

  public JsonNull(Object value) {
    this.value = value;
  }

  public Object getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return "null";
  }
}

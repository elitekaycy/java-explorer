package com.elitekaycy.json.model;

public class JsonNumber extends JsonValue {
  private final Number value;

  public JsonNumber(Number value) {
    this.value = value;
  }

  public Number getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}

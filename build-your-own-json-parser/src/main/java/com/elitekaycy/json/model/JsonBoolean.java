package com.elitekaycy.json.model;

public class JsonBoolean extends JsonValue {

  private final Boolean value;

  public JsonBoolean(Boolean value) {
    this.value = value;
  }

  public Object getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return value ? "true" : "false";
  }
}

package com.elitekaycy.json.model;

public class JsonKeyValue extends JsonValue {

  private final String key;
  private final Object value;

  public JsonKeyValue(String key, Object value) {
    this.key = key;
    this.value = value;
  }

  public Object getValue() {
    return value;
  }

  public String getKey() {
    return key;
  }

  @Override
  public String toString() {
    return "\"" + key + "\"" + ":" + value.toString();
  }
}

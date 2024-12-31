package com.elitekaycy.json.model;

import java.util.ArrayList;
import java.util.List;

public class JsonObject extends JsonValue {
  private List<JsonKeyValue> pairs;

  public JsonObject() {
    this.pairs = new ArrayList<>();
  }

  public void add(JsonKeyValue keyvalue) {
    pairs.add(keyvalue);
  }

  @Override
  public String toString() {
    StringBuilder objString = new StringBuilder();
    objString.append("{");

    for (JsonKeyValue kv : pairs) {
      objString.append(kv.toString()).append(",");
    }

    objString.deleteCharAt(objString.length() - 1);
    objString.append("}");
    return objString.toString();
  }
}

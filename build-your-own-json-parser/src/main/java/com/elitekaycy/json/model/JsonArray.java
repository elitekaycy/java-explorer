package com.elitekaycy.json.model;

import java.util.ArrayList;
import java.util.List;

public class JsonArray extends JsonValue {
  private List<Object> elements;

  public JsonArray() {
    this.elements = new ArrayList<>();
  }

  public void addElement(Object element) {
    this.elements.add(element);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    for (Object elem : elements) {
      builder.append(elem.toString()).append(",");
    }

    builder.deleteCharAt(builder.length() - 1);
    builder.append("]");
    return builder.toString();
  }
}

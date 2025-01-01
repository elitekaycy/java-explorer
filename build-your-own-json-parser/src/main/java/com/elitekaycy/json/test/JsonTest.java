package com.elitekaycy.json.test;

import com.elitekaycy.json.annotations.JsonExclude;
import com.elitekaycy.json.util.JavaToJsonMapper;
import java.util.List;

public class JsonTest {
  private static final String json =
      "{"
          + "\"name\": \"Smartphone\","
          + "\"price\": 699.99,"
          + "\"inStock\": true,"
          + "\"tags\": [\"electronics\", \"mobile\"],"
          + "\"supplier\": {"
          + "\"name\": \"Tech Corp\","
          + "\"country\": \"USA\""
          + "}"
          + "}";

  class Product {
    @JsonExclude private String name;
    private double price;
    private boolean inStock;
    private List<String> tags;
    private Supplier supplier;

    @Override
    public String toString() {
      return "Product {"
          + "name='"
          + name
          + '\''
          + ", price="
          + price
          + ", inStock="
          + inStock
          + ", tags="
          + tags
          + ", supplier="
          + supplier
          + '}';
    }
  }

  class Supplier {
    private String name;
    private String country;

    @Override
    public String toString() {
      return "Supplier {" + "name='" + name + '\'' + ", country='" + country + '\'' + '}';
    }
  }

  public static void test() {
    /* Here I try to map json String to Object class; **/
    Product product = (Product) JavaToJsonMapper.toObject(json, Product.class);

    System.out.println(product.toString());
  }
}

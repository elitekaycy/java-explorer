package com.elitekaycy.json.util;

import com.elitekaycy.json.annotations.JsonExclude;
import com.elitekaycy.json.lexer.Lexer;
import com.elitekaycy.json.model.*;
import com.elitekaycy.json.parser.Parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Utility class for mapping Java objects to JSON strings and vice versa.
 */
public class JavaToJsonMapper {

  /**
   * Converts a Java object into its JSON string representation.
   *
   * @param obj the Java object to convert
   * @return the JSON string representation of the object
   */
  public static String toJson(Object obj) {
    if (obj == null) {
      return "null";
    }

    Class<?> clazz = obj.getClass();

    // Handle primitive types, Strings, Numbers, and Booleans
    if (clazz.isPrimitive()
        || obj instanceof String
        || obj instanceof Number
        || obj instanceof Boolean) {
      return "\"" + obj.toString() + "\"";
    }

    // Handle collections like List and Set
    if (obj instanceof Collection) {
      StringBuilder json = new StringBuilder("[");
      Collection<?> collection = (Collection<?>) obj;
      for (Object element : collection) {
        json.append(toJson(element)).append(",");
      }
      if (!collection.isEmpty()) json.setLength(json.length() - 1);
      json.append("]");
      return json.toString();
    }

    // Handle maps like HashMap
    if (obj instanceof Map) {
      StringBuilder json = new StringBuilder("{");
      Map<?, ?> map = (Map<?, ?>) obj;
      for (Map.Entry<?, ?> entry : map.entrySet()) {
        json.append("\"")
            .append(entry.getKey())
            .append("\":")
            .append(toJson(entry.getValue()))
            .append(",");
      }
      if (!map.isEmpty()) json.setLength(json.length() - 1);
      json.append("}");
      return json.toString();
    }

    // Handle general Java objects
    StringBuilder json = new StringBuilder("{");
    Field[] fields = clazz.getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);
      try {
        Object fieldValue = field.get(obj);
        json.append("\"")
            .append(field.getName())
            .append("\":")
            .append(toJson(fieldValue))
            .append(",");
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    if (fields.length > 0) json.setLength(json.length() - 1);
    json.append("}");
    return json.toString();
  }

  /**
   * Parses a Java object into a JSON model object using the lexer and parser.
   *
   * @param obj the Java object to parse
   * @return the parsed JSON model object
   */
  public static Object parse(Object obj) {
    return new Parser(new Lexer(toJson(obj)).tokenize()).parse();
  }

  /**
   * Maps a JSON string to a Java object of the specified class.
   *
   * @param json the JSON string
   * @param cls the class of the Java object to map to
   * @param <T> the type of the Java object
   * @return the mapped Java object
   */
  public static <T> T toObject(String json, Class<T> cls) {
    JsonObject parser = (JsonObject) new Parser(new Lexer(json).tokenize()).parse();

    try {
      T object = createInstance(cls);
      for (JsonKeyValue kv : parser.getAll()) {
        try {
          Field field = cls.getDeclaredField(kv.getKey());
          if(field.isAnnotationPresent(JsonExclude.class)) continue;
          field.setAccessible(true);

          if (field.getType().equals(String.class) && kv.getValue() instanceof JsonString) {
            field.set(object, ((JsonString) kv.getValue()).getValue());
          } else if (Number.class.isAssignableFrom(field.getType())
              && kv.getValue() instanceof JsonNumber) {
            field.set(object, ((JsonNumber) kv.getValue()).getValue());
          } else if (field.getType().equals(Boolean.class)
              && kv.getValue() instanceof JsonBoolean) {
            field.set(object, ((JsonBoolean) kv.getValue()).getValue());
          } else if (kv.getValue() instanceof JsonNull) {
            field.set(object, null);
          } else if (kv.getValue() instanceof JsonArray) {
            Object parsedArray = parseArray((JsonArray) kv.getValue(), field);
            field.set(object, parsedArray);
          } else if (kv.getValue() instanceof JsonObject) {
            Object nestedObject = toObject(kv.getValue().toString(), field.getType());
            field.set(object, nestedObject);
          }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
      }

      return object;
    } catch (Exception e) {
      throw new RuntimeException("Failed to map JSON to object: " + e.getMessage(), e);
    }
  }

  /**
   * Creates an instance of the specified class using its default or first available constructor.
   *
   * @param cls the class to instantiate
   * @param <T> the type of the class
   * @return a new instance of the class
   * @throws Exception if instantiation fails
   */
  @SuppressWarnings("unchecked")
  private static <T> T createInstance(Class<T> cls) throws Exception {
    try {
      return cls.getDeclaredConstructor().newInstance();
    } catch (NoSuchMethodException e) {
      Constructor<?>[] constructors = cls.getDeclaredConstructors();
      if (constructors.length > 0) {
        Constructor<?> constructor = constructors[0];
        constructor.setAccessible(true);
        Object[] args = new Object[constructor.getParameterCount()];
        return (T) constructor.newInstance(args);
      }
      throw e;
    }
  }

  /**
   * Parses a JSON array into the appropriate Java array or list type.
   *
   * @param jsonArray the JSON array
   * @param field the field representing the array or list
   * @return the parsed Java array or list
   */
  private static Object parseArray(JsonArray jsonArray, Field field) {
    Class<?> fieldType = field.getType();

    if (fieldType.isArray()) {
      Class<?> componentType = fieldType.getComponentType();
      return handleArrayType(jsonArray, componentType);
    } else if (List.class.isAssignableFrom(fieldType)) {
      List<Object> result = new ArrayList<>();
      for (Object jsonElement : jsonArray.getAll()) {
        result.add(parseJsonElement(jsonElement));
      }
      return result;
    }

    throw new IllegalArgumentException("Field is not an array or list");
  }

  /**
   * Handles parsing JSON arrays into Java arrays.
   *
   * @param jsonArray the JSON array
   * @param componentType the component type of the array
   * @return the parsed Java array
   */
  private static Object handleArrayType(JsonArray jsonArray, Class<?> componentType) {
    List<Object> result = new ArrayList<>();
    for (Object jsonElement : jsonArray.getAll()) {
      result.add(parseJsonElement(jsonElement));
    }
    return result.toArray();
  }

  /**
   * Parses individual JSON elements into Java objects.
   *
   * @param jsonElement the JSON element
   * @return the parsed Java object
   */
  private static Object parseJsonElement(Object jsonElement) {
    if (jsonElement instanceof JsonObject) {
      return jsonElement.toString();
    } else if (jsonElement instanceof JsonString) {
      return ((JsonString) jsonElement).getValue();
    } else if (jsonElement instanceof JsonNumber) {
      return ((JsonNumber) jsonElement).getValue();
    } else if (jsonElement instanceof JsonBoolean) {
      return ((JsonBoolean) jsonElement).getValue();
    } else if (jsonElement instanceof JsonNull) {
      return null;
    }
    return jsonElement;
  }
}


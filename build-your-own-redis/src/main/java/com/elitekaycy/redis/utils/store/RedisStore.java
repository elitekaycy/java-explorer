package utils.store;

public interface RedisStore {

  void set(String key, String value);

  String get(String key);

  void hset(String key, String field, String value);

  String hget(String key, String field);
}

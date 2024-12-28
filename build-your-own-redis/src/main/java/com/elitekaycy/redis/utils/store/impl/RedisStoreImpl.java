package utils.store.impl;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import utils.store.RedisStore;

public class RedisStoreImpl implements RedisStore {

  private final Map<String, String> strings = new ConcurrentHashMap<>();
  private final Map<String, Map<String, String>> hashes = new ConcurrentHashMap<>();

  @Override
  public void set(String key, String value) {
    strings.put(key, value);
  }

  @Override
  public String get(String key) {
    return strings.get(key);
  }

  @Override
  public void hset(String key, String field, String value) {
    hashes.computeIfAbsent(key, k -> new ConcurrentHashMap<String, String>()).put(field, value);
  }

  @Override
  public String hget(String key, String field) {
   return Optional.ofNullable(hashes.get(key)).map(hash -> hash.get(field)).orElse(null);
  }
}

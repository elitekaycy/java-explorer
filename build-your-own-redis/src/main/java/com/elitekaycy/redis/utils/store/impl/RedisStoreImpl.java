package utils.store.impl;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import utils.store.RedisStore;

public class RedisStoreImpl implements RedisStore {

  private static final Map<String, String> strings = new ConcurrentHashMap<>();
  private static final Map<String, Map<String, String>> hashes = new ConcurrentHashMap<>();

  private RedisStoreImpl() {}

  private static class RedisStoreHolder {
    private static final RedisStoreImpl INSTANCE = new RedisStoreImpl();
  }

  public static RedisStoreImpl getInstance() {
    return RedisStoreHolder.INSTANCE;
  }

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
    hashes.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).put(field, value);
  }

  @Override
  public String hget(String key, String field) {
    return Optional.ofNullable(hashes.get(key)).map(hash -> hash.get(field)).orElse(null);
  }
}

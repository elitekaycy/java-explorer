package com.elitekaycy.cache.service.impl;

import com.elitekaycy.cache.service.Cache;
import com.elitekaycy.cache.util.SimpleCacheEntry;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleCache<K, V> implements Cache<K, V> {
  private final ConcurrentHashMap<K, SimpleCacheEntry<V>> cache = new ConcurrentHashMap<>();

  @Override
  public void put(K key, V value) {
    cache.put(key, new SimpleCacheEntry<V>(value));
  }

  @Override
  public V get(K key) {
    return cache.get(key).value();
  }

  @Override
  public void remove(K key) {
    cache.remove(key);
  }

  protected ConcurrentHashMap<K, SimpleCacheEntry<V>> getCache() {
    return cache;
  }
}

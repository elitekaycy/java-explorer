package com.elitekaycy.cache.service.impl;

import com.elitekaycy.cache.util.SimpleCacheEntry;

public class TtlCache<K, V> extends SimpleCache<K, V> {

  private final long ttlMillis;

  public TtlCache(long ttlMillis) {
    this.ttlMillis = ttlMillis;
  }

  public TtlCache() {
    this.ttlMillis = System.currentTimeMillis() + 24;
  }

  @Override
  public void put(K key, V value) {
    super.put(key, value);
  }

  @Override
  public V get(K key) {

    SimpleCacheEntry<V> entry = super.getCache().get(key);
    if (isExpired(entry)) {
      super.remove(key);
      return null;
    }

    return entry.value();
  }

  @Override
  public void remove(K key) {
    super.remove(key);
  }

  protected boolean isExpired(SimpleCacheEntry<V> entry) {
    return (System.currentTimeMillis() - entry.timestamp()) > ttlMillis;
  }

  public Long getTtlMillis() {
    return ttlMillis;
  }
}

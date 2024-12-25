package com.elitekaycy.cache.util;

/* Wrapper for cache input**/
public record SimpleCacheEntry<V>(V value, long timestamp) {
  public SimpleCacheEntry(V value) {
    this(value, System.currentTimeMillis());
  }
}

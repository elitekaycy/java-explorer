package com.elitekaycy.cache;

import com.elitekaycy.cache.service.impl.EvictionCache;

public class SimpleCache {

  public static void main(String[] args) {

    EvictionCache<String, String> cache = new EvictionCache<>(3, 10000);

    cache.put("1", "One");
    cache.put("2", "Two");
    cache.put("3", "Three");

    System.out.println("Accessed: " + cache.get("1"));

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    cache.put("4", "Four");
    System.out.println("Cache size after eviction: " + cache.getSize());

    try {
      Thread.sleep(12000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    System.out.println("Accessed (TTL check): " + cache.get("1"));
  }
}

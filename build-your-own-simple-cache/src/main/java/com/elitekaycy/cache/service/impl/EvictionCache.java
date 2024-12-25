package com.elitekaycy.cache.service.impl;

import com.elitekaycy.cache.util.SimpleCacheEntry;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EvictionCache<K, V> extends TtlCache<K, V> {
  private final int capacity;
  private final LinkedHashMap<K, Long> accessOrder;
  private final Lock evictionLock;
  private final ScheduledExecutorService cleaner;

  public EvictionCache(int capacity, long ttlMillis) {

    super(ttlMillis);
    this.capacity = capacity;
    this.accessOrder = new LinkedHashMap<>(capacity, 0.75f, true);
    this.evictionLock = new ReentrantLock();

    this.cleaner = Executors.newScheduledThreadPool(1);
    cleaner.scheduleAtFixedRate(this::evictExpired, 0, 1, TimeUnit.MINUTES);
  }

  public int getSize() {
    return super.getCache().size();
  }

  @Override
  public void put(K key, V value) {
    evictionLock.lock();
    try {
      if (super.getCache().size() >= capacity) {
        evictLRU();
      }

      super.put(key, value);
      accessOrder.put(key, System.nanoTime());

    } finally {
      evictionLock.unlock();
    }
  }

  @Override
  public V get(K key) {
    evictionLock.lock();
    try {
      accessOrder.put(key, System.nanoTime());
      return super.get(key);
    } finally {
      evictionLock.unlock();
    }
  }

  @Override
  public void remove(K key) {
    super.remove(key);
  }

  protected void evictLRU() {
    if (super.getCache().size() > capacity) {
      K lruKey =
          Collections.min(accessOrder.entrySet(), Comparator.comparingLong(Map.Entry::getValue))
              .getKey();
      super.remove(lruKey);
      accessOrder.remove(lruKey);
    }
  }

  protected void evictExpired() {

    evictionLock.lock();
    try {
      long currentTime = System.currentTimeMillis();
      java.util.Iterator<Map.Entry<K, SimpleCacheEntry<V>>> iterator =
          super.getCache().entrySet().iterator();

      while (iterator.hasNext()) {

        Map.Entry<K, SimpleCacheEntry<V>> entry = iterator.next();
        if (super.isExpired(entry.getValue())) {
          iterator.remove();
          accessOrder.remove(entry.getKey());
        }
      }
    } finally {
      evictionLock.unlock();
    }
  }
}

package com.elitekaycy.cache.service;

/**
 * Represents a generic Cache interface for storing and retrieving key-value pairs.
 *
 * <p>This interface provides method definitions for basic cache operations, including adding,
 * retrieving, and removing items from the cache. Implementing classes should define the specific
 * behavior for these operations, such as storage mechanisms and eviction policies.
 *
 * @param <K> the type of keys used to identify cached values
 * @param <V> the type of values stored in the cache
 * @author Dickson Anyaele
 * @version 1.0
 */
public interface Cache<K, V> {

  /**
   * Adds a key-value pair to the cache. If the key already exists, its value should be updated with
   * the provided value.
   *
   * @param key the key used to identify the value in the cache
   * @param value the value to be stored in the cache
   * @throws NullPointerException if the key or value is {@code null}
   */
  void put(K key, V value);

  /**
   * Retrieves the value associated with the specified key from the cache.
   *
   * @param key the key whose associated value is to be returned
   * @return the value associated with the specified key, or {@code null} if the key does not exist
   *     in the cache
   * @throws NullPointerException if the key is {@code null}
   */
  V get(K key);

  /**
   * Removes the key-value pair associated with the specified key from the cache.
   *
   * @param key the key whose associated value is to be removed
   * @throws NullPointerException if the key is {@code null}
   */
  void remove(K key);
}

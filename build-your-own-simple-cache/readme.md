### Simple Caching (With logging Proxy)

This project demonstrates how to build a simple in-memory cache with logging functionality using the proxy pattern. The cache allows for basic operations (put, get, remove) and logs all interactions for auditing or debugging purposes. The logging functionality is decoupled from the actual cache, allowing flexibility and easier maintenance.


#### Features
 - ```put(K key, V value)``` : Store a key value pair in the cache
 - ```get(K key)```: Retrivev a value by key
 - ```remove (K key)```: Remove a key value pair from cache

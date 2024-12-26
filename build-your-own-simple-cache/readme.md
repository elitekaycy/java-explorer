# Simple Caching with Eviction Policy

This project demonstrates a **Simple Caching System** with a focus on efficient memory management using an [**Eviction Cache**](./src/main/java/com/elitekaycy/cache/service/impl/EvictionCache.java). The cache supports **basic operations** (`put`, `get`, `remove`) and ensures optimal performance through **Least Recently Used (LRU)** eviction and **timestamp-based eviction** policies.

## Features

### Core Cache Operations
- **`put(K key, V value)`**: Stores a key-value pair in the cache.
- **`get(K key)`**: Retrieves a value by its key.
- **`remove(K key)`**: Removes a key-value pair from the cache.

### Eviction Cache
- **Thread-Safe Operations**: Uses `ConcurrentHashMap` and `ReentrantLocks` for synchronization.
- **Eviction Strategies**:
  - **Outdated Timestamp Eviction**: Removes entries that exceed a predefined age.
  - **LRU Eviction**: Removes the least recently used entries when the cache reaches its maximum capacity.
- **Scheduler Thread**: A dedicated thread periodically checks and removes stale entries based on timestamps.

---

## How It Works

1. **Simple Cache**:
   - Implements basic cache operations with a focus on simplicity and correctness.

2. **Eviction Cache**:
   - Extends the functionality of the simple cache by introducing eviction policies and synchronization mechanisms.
   - Uses `LinkedHashMap` to maintain insertion/access order for LRU eviction.
   - A scheduled thread periodically checks for and removes outdated entries.

---

## How to Build and Run the Project

### Steps to Build and Run
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/elitekaycy/java-explorer
   cd build-your-own-simple-cache 
   ```

2. **Build the Project**:
   Run the following Bash script to compile the source code:
   ```bash
   ./build.sh
   ```
   This script:
   - Creates an `out` directory for compiled files.
   - Compiles the Java source files in `src/main/java` into the `out` directory.

3. **Run the Application**:
   If compilation succeeds, the script automatically runs the module:
   ```bash
   java --module-path out -m cache/com.elitekaycy.cache.SimpleCache
   ```

---

## How the Code Works

### Example Usage
```java
// Creating the cache
EvictionCache<String, String> cache = new EvictionCache<>(100, 10_000); // Max capacity: 100, TTL: 10 seconds

// Adding entries
cache.put("1", "First Entry");
System.out.println(cache.get("1")); // Retrieves the value
cache.remove("1"); // Removes the entry

// TTL Eviction
cache.put("2", "Second Entry");
Thread.sleep(11_000); // Simulate time passing
System.out.println(cache.get("2")); // Returns null (entry evicted due to TTL)

// LRU Eviction
for (int i = 0; i < 120; i++) {
    cache.put(String.valueOf(i), "Value " + i);
}
System.out.println(cache.size()); // Ensures cache size is capped at 100
```

---

## Key Concepts and Libraries Used
- **ConcurrentHashMap**: Ensures thread-safe operations for high-performance cache access.
- **ReentrantLock**: Adds fine-grained synchronization for eviction logic.
- **LinkedHashMap**: Maintains access order for implementing LRU eviction.

---

## Future Enhancements
- Add persistence support for caching data to disk.
- Extend to distributed caching with frameworks like Redis.

---

## Author
**Dickson Anyaele (elite-kaycy)**  
For contributions or inquiries, feel free to reach out!

---


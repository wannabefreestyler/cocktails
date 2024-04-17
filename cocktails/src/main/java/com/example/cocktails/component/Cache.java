package com.example.cocktails.component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class Cache<K, V> {
  private static final int MAX_ENTRIES = 10;

  private final Map<K, Object> cocktailCache = new LinkedHashMap<>(MAX_ENTRIES, 0.75f, true) {
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
      return size() > MAX_ENTRIES;
    }
  };

  public void put(K key, V value) {
    cocktailCache.put(key, value);
  }

  public void putList(K key, List<V> valueList) {
    cocktailCache.put(key, valueList);
  }

  public V get(K key) {
    return (V) cocktailCache.get(key);
  }

  public List<V> getList(K key) {
    return (List<V>) cocktailCache.get(key);
  }

  public boolean containsKey(K key) {
    return cocktailCache.containsKey(key);
  }

  public void remove(K key) {
    cocktailCache.remove(key);
  }

  public void clear() {
    cocktailCache.clear();
  }
}
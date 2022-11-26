package structures;

/**
 * The Dictionary interface.
 *
 * @param <K> the key class type.
 * @param <V> the value class type.
 * @author André Gabriel
 */
public interface Dictionary<K, V> {

  /**
   * Returns the number of key-value mappings in this map. If the
   *
   * <p>map contains more than {@code Integer.MAX_VALUE} elements, returns
   *
   * <p>{@code Integer.MAX_VALUE}.
   *
   * @return the number of key-value mappings in this map
   */
  int size();

  /**
   * Returns {@code true} if the map contains a specified key.
   *
   * @param key the key to check is on the map.
   * @return {@code true} if this map contains the specified Key or {@code false } otherwise.
   * @throws ClassCastException if the key is of an inappropriate type this map.
   * @throws NullPointerException if the specified key is null.
   */
  boolean containsKey(Object key);

  /**
   * Return the value mapped to the given key or {@code null} if not in the map.
   *
   * @param key the key associated with the value.
   * @return {@code V} the value associated with the {@code key} or {@code null}, otherwise.
   * @throws ClassCastException if the value is of an inappropriate type this map.
   * @throws NullPointerException if the specified key is null.
   */
  V get(K key);

  /**
   * Add a key-value entry on the map. If the map previously contained a mapping for the key, the
   * old value is replaced by the specified value.
   *
   * @param key the key associated with the value.
   * @param value the value associated with the key.
   * @return {@code V} the previous value associated with {@code key} or {@code null}, otherwise.
   * @throws ClassCastException if the value is of an inappropriate type this map.
   * @throws NullPointerException if the specified key is null.
   */
  V insert(K key, V value);

  /**
   * Remove a key-value entry on the map.
   *
   * @param key the key associated with the value.
   * @return {@code V} the previous value associated with {@code key} or {@code null}, otherwise.
   * @throws ClassCastException if the value is of an inappropriate type this map.
   * @throws NullPointerException if the specified key is null.
   */
  V remove(Object key);

  /**
   * Defines a dictionary entry (key-value pair) where the values may or may not be immutable.
   *
   * @param <K> the type of the key
   * @param <V> the type of the value
   */
  interface Entry<K, V> {
    /**
     * Returns the key corresponding to this entry.
     *
     * @return the key corresponding to this entry
     */
    K getKey();
    /**
     * Returns the value corresponding to this entry.
     *
     * @return the value corresponding to this entry
     */
    V getValue();
    /**
     * Replaces the value corresponding to this entry with the specified
     *
     * @param value new value to be stored in this entry
     * @return old value corresponding to the entry
     * @throws NullPointerException if the backing map does not permit null values, and the
     *     specified value is null
     * @throws ClassCastException if the class of the specified value prevents it from being stored
     *     in the backing map
     */
    V setValue(V value);

    /**
     * Compares the specified object with this entry for equality.
     *
     * @param o object to be compared for equality with this map entry
     * @return {@code true} if the specified object is equal.
     */
    boolean equals(Object o);
  }
}

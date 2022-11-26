package structures;

import java.util.Objects;

public class HashTableList<K, V> implements Dictionary<K, V> {

  /** The default initial capacity - MUST be a power of two. */
  static final int DEFAULT_INITIAL_CAPACITY = 16;

  /** The maximum capacity - MUST be a power of two. */
  static final int MAXIMUM_CAPACITY = 1 << 30;

  /** The load factor. */
  static final float DEFAULT_LOAD_FACTOR = 0.75f; // 75%

  /** The number of entries contained in this map. */
  private int size;

  /**
   * The LinkedList Node.
   *
   * <p>That is an implementation of Dictionary Entry.
   *
   * @param <K> the key class type.
   * @param <V> the value class type.
   * @see Dictionary
   */
  static class Node<K, V> implements Dictionary.Entry<K, V> {
    final int hash;
    final K key;
    V value;
    Node<K, V> next;

    public Node(int p_hash, K p_key, V p_value, Node<K, V> p_next) {
      this.hash = p_hash;
      this.key = p_key;
      this.value = p_value;
      this.next = p_next;
    }

    @Override
    public K getKey() {
      return this.key;
    }

    @Override
    public V getValue() {
      return this.value;
    }

    @Override
    public V setValue(V new_value) {
      V old_value = this.value;
      this.value = new_value;
      return old_value;
    }

    @Override
    public final boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Dictionary.Entry<?, ?>) {
        Dictionary.Entry<?, ?> entry = (Dictionary.Entry<?, ?>) o;
        return Objects.equals(this.key, entry.getKey())
            && Objects.equals(this.value, entry.getValue());
      }
      return false;
    }

    @Override
    public final String toString() {
      return "<" + this.key + ":" + this.value + ">";
    }

    @Override
    public final int hashCode() {
      return Objects.hashCode(this.key) ^ Objects.hashCode(this.value);
    }
  }

  /**
   * Return a hash for a giving key.
   *
   * <p>This method uses a key.hashCode() function to get a hash. Then spreads the higher bits of
   * hash to lower using a XOR. Avoiding bad hash functions. Because in most times these bit are not
   * used in index calculations.
   *
   * @param key the key to be hashed;
   * @return the hash value for the {@code key}.
   */
  private static int hash(Object key) {
    int h = key.hashCode();
    return ((h) ^ (h >>> 16));
  }

  /**
   * Returns a power of two size for the given target capacity.
   *
   * <p>This take the capacity sub 1 and do a {@code OR} with the shifted value of n.
   *
   * <p>E.g. if {@code cap = 10}, then {@code n = 9} so
   *
   * <p>1001 | 0100 -> 1101;
   *
   * <p>1101 | 0010 -> 1111 aka 15.
   *
   * <p>Then returns 16.
   *
   * @param cap the value of table capacity.
   * @return the power of two nearest to the given value
   */
  private static int tableSizeFor(int cap) {
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
  }

  /** The internal table. */
  private Node<K, V>[] table;

  /** The next size value at which to resize (capacity * load factor). */
  private int threshold;
  /** The load factor for the hash table. */
  private float load_factor;

  public HashTableList(int initial_capacity, float p_load_factor) {
    if (initial_capacity < 0) {
      throw new IllegalArgumentException("Illegal initial capacity: " + initial_capacity);
    }

    if (initial_capacity > MAXIMUM_CAPACITY) {
      initial_capacity = MAXIMUM_CAPACITY;
    }
    if (p_load_factor <= 0 || Float.isNaN(p_load_factor)) {
      throw new IllegalArgumentException("Illegal load factor: " + load_factor);
    }
    this.load_factor = p_load_factor;
    this.threshold = tableSizeFor(initial_capacity);
  }

  public HashTableList(int initial_capacity) {
    this(initial_capacity, DEFAULT_LOAD_FACTOR);
  }

  public HashTableList() {
    this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean containsKey(Object key) {
    return getNode(key) != null;
  }

  @Override
  public V get(K key) {
    final Node<K, V> e = getNode(key);
    return e == null ? null : e.getValue();
  }

  @Override
  public V insert(K key, V value) {
    if (key == null) {
      throw new NullPointerException("Illegal key value");
    }
    return putVal(hash(key), key, value);
  }

  @Override
  public V remove(Object key) {
    return null;
  }

  /**
   * Return a noe from a given key or {@code null} if the key is not it is not in the table;
   *
   * @param key the key to be search a value.
   * @return the value mapped to the key or {@code null}
   * @throws NullPointerException if the key is null.
   */
  private Node<K, V> getNode(Object key) {

    if (key == null) {
      throw new NullPointerException("Illegal key value");
    }

    Node<K, V>[] tab;
    int n;
    if ((tab = table) != null && (n = tab.length) > 0) {
      int hash = hash(key);
      int index = (n - 1) & (hash);
      Node<K, V> current = tab[index];

      if (current == null) {
        return null;
      }

      if (current.hash == hash && (current.key == key || key.equals(current.key))) {
        return current;
      }

      // May be same hash, but not same key.
      do {
        if (current.hash == hash && (current.key == key || key.equals(current.key))) {
          return current;
        }
      } while ((current = current.next) != null);
    }
    return null;
  }

  // Create a regular node
  private Node<K, V> newNode(int hash, K key, V value) {
    return new Node<>(hash, key, value, null);
  }

  /**
   * Add a new entry in the map if the current key is on the map replace the value.
   *
   * @param hash the key hash code to put in map.
   * @param key the key to put in the map.
   * @param value the value to be associated to the key.
   * @return the old value if the key is on the map or {@code null} otherwise.
   */
  private V putVal(int hash, K key, V value) {

    Node<K, V>[] tab;

    if ((tab = table) == null || (tab.length) == 0) {
      tab = resize();
    }

    int n = tab.length;
    int index = (n - 1) & hash;

    if (tab[index] == null) {
      tab[index] = newNode(hash, key, value);
    } else {
      Node<K, V> current;

      // Always check the first
      if ((current = tab[index]).hash == hash && (current.key == key || key.equals(current.key))) {
        V old_value = current.getValue();
        current.setValue(value);
        return old_value;
      }

      // Find in the list a node with same key
      while (current.next != null) {
        if (current.hash == hash && (current.key == key || key.equals(current.key))) {
          V old_value = current.getValue();
          current.setValue(value);
          return old_value;
        }
        current = current.next;
      }
      current.next = newNode(hash, key, value);
    }

    if (++size > threshold) {
      resize();
    }

    return null;
  }

  private Node<K, V>[] resize() {

    Node<K, V>[] old_table = table;
    int old_cap = (old_table != null) ? old_table.length : 0;
    int old_trh = threshold;
    int new_cap = 0;
    int new_thr = 0;

    // Resize only
    if (old_cap > 0) {
      // If in the maximum capacity set threshold to maximum else double the size.
      if (old_cap >= MAXIMUM_CAPACITY) {
        threshold = Integer.MAX_VALUE;
        return old_table;
      } else if ((old_cap << 1) < MAXIMUM_CAPACITY && old_cap >= DEFAULT_INITIAL_CAPACITY) {
        new_cap = old_cap << 1;
        new_thr = old_trh << 1;
      }
    } else if (old_trh == 0) { // if the initial capacity is 0
      new_cap = DEFAULT_INITIAL_CAPACITY;
      new_thr = (int) (DEFAULT_INITIAL_CAPACITY * load_factor);
    } else { // initial capacity was placed in threshold
      new_cap = old_trh;
      float ft = (float) new_cap * load_factor;
      new_thr =
          (new_cap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY
              ? (int) ft
              : Integer.MAX_VALUE);
    }

    threshold = new_thr;
    @SuppressWarnings({"unchecked"})
    Node<K, V>[] new_table = (Node<K, V>[]) new Node[new_cap];
    table = new_table;

    // Copy elements
    if (old_table != null) {
      for (int i = 0; i < old_cap; ++i) {
        Node<K, V> current = old_table[i];
        if ((current) != null) {
          old_table[i] = null;
          int index = current.hash & (new_cap - 1);
          if (current.next == null) {
            new_table[index] = current;
          } else {
            Node<K, V> low_head = null, low_tail = null;
            Node<K, V> hi_head = null, hi_tail = null;
            Node<K, V> next;
            do {
              next = current.next;
              if ((current.hash & old_cap)
                  == 0) { // Check if hash is the same position on new table.
                if (low_tail == null) {
                  low_head = current;
                } else {
                  low_tail.next = current;
                }
                low_tail = current;
              } else {
                if (hi_tail == null) {
                  hi_head = current;
                } else {
                  hi_tail.next = current;
                }
                hi_tail = current;
              }
            } while ((current = next) != null);

            if (low_tail != null) {
              low_tail.next = null;
              new_table[i] = low_head;
            }
            if (hi_tail != null) {
              hi_tail.next = null;
              new_table[i + old_cap] = hi_head;
            }
          }
        }
      }
    }
    return new_table;
  }

  /**
   * Remove a key-value entry on the map.
   *
   * @param hash the hash value for the key.
   * @param key the key to be removed.
   * @return old value of key if is in the map or {@code null} otherwise.
   */
  private Node<K, V> removeNode(int hash, Object key) {
    Node<K, V>[] tab;
    if ((tab = table) != null && tab.length > 0) {
      final int n = tab.length;
      int index = (n - 1) & hash;
      Node<K, V> current = tab[index];

      Node<K, V> previous;

      if (current == null) {
        return null;
      }

      if (current.hash == hash && (current.key == key || key.equals(current.key))) {
        tab[index] = current.next;
        --size;
        return current;
      } else if (current.next != null) {
        previous = current;
        do {
          if (current.hash == hash && (current.key == key || key.equals(current.key))) {
            previous.next = current.next;
            --size;
            return current;
          }
          previous = current;
        } while ((current = current.next) != null);
      }
    }
    return null;
  }

  /** @return the actual capacity of table. */
  public int capacity() {
    return table == null ? 0 : table.length;
  }

  /** Prints the map. */
  public void print() {
    if (table == null || table.length == 0) System.out.print("Table is null!");
    else {
      for (int i = 0; i < table.length; i++) {
        if (table[i] == null) {
          System.out.printf("%d -> %s\n", i, "null");
        } else {
          System.out.printf("%d -> ", i);
          Node<K, V> cursor = table[i];
          do {
            System.out.printf("{ %s, %s } -> ", cursor.getKey(), cursor.getValue());
          } while ((cursor = cursor.next) != null);
          System.out.print("null\n");
        }
      }
    }
  }
}

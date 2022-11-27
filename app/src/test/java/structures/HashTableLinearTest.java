package structures;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HashTableLinearTest {

  @Test
  void shouldHaveCorrectSize() {
    HashTableLinear<String, Integer> classUnderTest = new HashTableLinear<>();
    assertEquals(0, classUnderTest.size(), "HashTable must be have correct size!");
  }

  @Test
  void shouldHaveCorrectCapacities() {
    HashTableList<String, Integer> classUnderTest = new HashTableList<>();
    assertEquals(0, classUnderTest.capacity(), "HashTable must be have capacity equals to 0!");

    classUnderTest = new HashTableList<>();
    classUnderTest.insert("test", 0); // Initialize the HashTable
    assertEquals(16, classUnderTest.capacity(), "HashTable must be have capacity equals to 16!");

    classUnderTest = new HashTableList<>(12);
    classUnderTest.insert("test", 0); // Initialize the HashTable
    assertEquals(16, classUnderTest.capacity(), "HashTable must be have capacity equals to 16!");

    classUnderTest = new HashTableList<>(17);
    classUnderTest.insert("test", 0); // Initialize the HashTable
    assertEquals(32, classUnderTest.capacity(), "HashTable must be have capacity equals to 32!");
  }

  @Test
  void shouldInsertCorrectly() {
    HashTableList<String, Integer> classUnderTest = new HashTableList<>();
    classUnderTest.insert("void", 0);
    classUnderTest.insert("int", 2);
    classUnderTest.insert("float", 3);
    classUnderTest.insert("bool", 40);
    assertTrue(classUnderTest.containsKey("void"), "HashTable must insert correctly!");
    assertTrue(classUnderTest.containsKey("int"), "HashTable must insert correctly!");
    assertTrue(classUnderTest.containsKey("float"), "HashTable must insert correctly!");
    assertTrue(classUnderTest.containsKey("bool"), "HashTable must insert correctly!");
  }

  @Test
  void shouldHaveCorrectSizeAfterInsert() {
    HashTableLinear<String, Integer> classUnderTest = new HashTableLinear<>();
    classUnderTest.insert("void", 0);
    classUnderTest.insert("int", 2);
    classUnderTest.insert("float", 3);
    classUnderTest.insert("bool", 40);
    assertEquals(4, classUnderTest.size(), "HashTable must be size 4!");
  }

  @Test
  void shouldHaveCorrectSizeAfterRemove() {
    HashTableLinear<String, Integer> classUnderTest = new HashTableLinear<>();
    classUnderTest.insert("void", 0);
    classUnderTest.insert("int", 2);
    classUnderTest.insert("float", 3);
    classUnderTest.insert("bool", 40);
    classUnderTest.remove("float");
    assertEquals(3, classUnderTest.size(), "HashTable must be size 3!");
  }

  @Test
  void shouldTreatCollisions() {
    HashTableLinear<Integer, String> classUnderTest = new HashTableLinear<>();
    classUnderTest.insert(0, "Heapsort");
    classUnderTest.insert(4, "Quicksort");
    classUnderTest.insert(3, "Mergesort");
    classUnderTest.insert(35, "SelectionSort");
    assertEquals(4, classUnderTest.size(), "HashTable must be size 4");
    assertTrue(classUnderTest.containsKey(0), "HashTable must contains key 0");
    assertTrue(classUnderTest.containsKey(4), "HashTable must contains key 4");
    assertTrue(classUnderTest.containsKey(3), "HashTable must contains key 3");
    assertTrue(classUnderTest.containsKey(35), "HashTable must contains key 35");
  }

  @Test
  void shouldRemoveAfterCollision() {
    HashTableLinear<Integer, String> classUnderTest = new HashTableLinear<>();
    classUnderTest.insert(0, "Heapsort");
    classUnderTest.insert(16, "TreeSort");
    classUnderTest.insert(4, "Quicksort");
    classUnderTest.insert(20, "RadixSort");
    classUnderTest.insert(3, "Mergesort");
    classUnderTest.insert(19, "BucketSort");
    classUnderTest.insert(35, "SelectionSort");
    classUnderTest.insert(51, "BubbleSort");

    // Check insertions
    assertEquals(8, classUnderTest.size(), "HashTable must be size 8!");

    classUnderTest.remove(0);
    classUnderTest.remove(4);
    classUnderTest.remove(35);

    // Check remaining keys
    assertEquals(5, classUnderTest.size(), "HashTable must be size 5");
    assertTrue(classUnderTest.containsKey(16), "HashTable must contains key 16");
    assertTrue(classUnderTest.containsKey(20), "HashTable must contains key 20");
    assertTrue(classUnderTest.containsKey(3), "HashTable must contains key 3");
    assertTrue(classUnderTest.containsKey(51), "HashTable must contains key 51");

    // Check if is it removed
    assertFalse(classUnderTest.containsKey(0), "HashTable must not have key 0");
    assertFalse(classUnderTest.containsKey(4), "HashTable must not have key 4");
    assertFalse(classUnderTest.containsKey(35), "HashTable must not have key 51");
  }

  @Test
  void hashShouldInsertKeyAfterKeyIsRemoved() {
    HashTableLinear<Integer, String> classUnderTest = new HashTableLinear<>();
    classUnderTest.insert(0, "Heapsort");
    classUnderTest.insert(16, "TreeSort");
    classUnderTest.insert(4, "Quicksort");
    classUnderTest.insert(20, "RadixSort");
    classUnderTest.insert(3, "Mergesort");
    classUnderTest.insert(19, "BucketSort");
    classUnderTest.insert(35, "SelectionSort");
    classUnderTest.insert(51, "BubbleSort");
    assertEquals(8, classUnderTest.size(), "HashTable must be size 8!");

    classUnderTest.remove(0);
    classUnderTest.remove(4);

    assertEquals(6, classUnderTest.size(), "HashTable must be size 6");
    assertTrue(classUnderTest.containsKey(16), "HashTable must contains key 16");
    assertTrue(classUnderTest.containsKey(20), "HashTable must contains key 20");
    assertTrue(classUnderTest.containsKey(3), "HashTable must contains key 3");
    assertTrue(classUnderTest.containsKey(51), "HashTable must contains key 51");

    classUnderTest.insert(32, "SimpleSort");
    classUnderTest.insert(36, "StackTrace");

    assertEquals(8, classUnderTest.size(), "HashTable must be size 8");
    assertTrue(classUnderTest.containsKey(32), "HashTable must contains key 32");
    assertTrue(classUnderTest.containsKey(36), "HashTable must contains key 36");
  }

  @Test
  void shouldDoubleCapacityCorrectly() {
    HashTableLinear<Integer, String> classUnderTest = new HashTableLinear<>();
    for (int i = 0; i < 13; i++) {
      char c = (char) (i + 65);
      classUnderTest.insert(i, String.valueOf(c));
    }
    assertEquals(32, classUnderTest.capacity(), "HashTable must be capacity 32");
    classUnderTest = new HashTableLinear<>(16, 0.5f);
    for (int i = 0; i < 9; i++) {
      char c = (char) (i + 65);
      classUnderTest.insert(i, String.valueOf(c));
    }
    assertEquals(32, classUnderTest.capacity(), "HashTable must be capacity 32");
  }

  @Test
  void shouldTraitCollisionsAfterResize() {
    HashTableLinear<Integer, String> classUnderTest = new HashTableLinear<>();
    classUnderTest.insert(0, "Heapsort");
    classUnderTest.insert(16, "TreeSort");
    classUnderTest.insert(4, "Quicksort");
    classUnderTest.insert(20, "RadixSort");
    classUnderTest.insert(3, "Mergesort");
    classUnderTest.insert(19, "BucketSort");
    classUnderTest.insert(35, "SelectionSort");
    classUnderTest.insert(51, "BubbleSort");

    for (int i = 10; i < 15; ++i) {
      char c = (char) (i + 65);
      classUnderTest.insert(i, String.valueOf(c));
    }

    assertEquals(32, classUnderTest.capacity(), "HashTable must be capacity 32");
    assertTrue(classUnderTest.containsKey(3), "HashTable must have key 3 ");
    assertTrue(classUnderTest.containsKey(35), "HashTable must have key 35");
  }
}

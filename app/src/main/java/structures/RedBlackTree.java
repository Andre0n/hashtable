package structures;

import java.util.Objects;

/**
 * The red-black tree implementation.
 *
 * @author André Gabriel
 * @see Dictionary.Entry
 * @see HashTableRB
 */
public class RedBlackTree<K extends Comparable<K>, V> {

  /*
   * The root of the tree;
   */
  private TreeNode<K, V> root;

  /** The constructor method. */
  public RedBlackTree() {
    root = null;
  }

  /**
   * The red-black tree Node.
   *
   * <p>That is an implementation of Dictionary Entry.
   *
   * @param <K> the key class type.
   * @param <V> the value class type.
   * @see Dictionary
   */
  private class TreeNode<K, V> implements Dictionary.Entry<K, V> {

    final int hash;
    final K key;
    V value;
    TreeNode<K, V> left;
    TreeNode<K, V> right;
    TreeNode<K, V> parent;
    boolean red;

    public TreeNode(int p_hash, K p_key, V p_value) {
      this.hash = p_hash;
      this.key = p_key;
      this.value = p_value;
      this.left = null;
      this.right = null;
      this.parent = null;
      this.red = true;
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
      return "TreeNode<" + this.red + ", " + this.key + ":" + this.value + ">";
    }
  }

  /**
   * Put a new node in the tree, or update a node value.
   *
   * @param hash the hashcode of the entry.
   * @param key the key of the entry.
   * @param value the value of the entry.
   * @return an old entry value if it exists or {@code null} instead.
   */
  private V putTreeVal(int hash, K key, V value) {
    int dir = 0;
    TreeNode<K, V> cursor = this.root, prev = null;
    while (cursor != null) {
      K ck;
      TreeNode<K, V> cl = cursor.left, cr = cursor.right;
      prev = cursor;
      if ((ck = cursor.key) == key || (key != null && Objects.equals(key, ck))) {
        return cursor.setValue(value);
      } else if (key != null && ck.compareTo(key) > 0) {
        dir = -1;
      } else if (key != null && ck.compareTo(key) < 0) {
        dir = 1;
      } else {
        return cursor.setValue(value);
      }
      cursor = dir < 0 ? cl : cr;
    }
    TreeNode<K, V> n = new TreeNode<>(hash, key, value);
    n.parent = prev;
    if (prev == null) {
      this.root = n;
    } else if (dir < 0) {
      prev.left = n;
    } else {
      prev.right = n;
    }
    fixInsert(n);
    return null;
  }

  /**
   * Fixup the tree, recoloring and rotating the nodes.
   *
   * @param k the node to be fixed;
   */
  private void fixInsert(TreeNode<K, V> k) {
    k.red = true;
    for (TreeNode<K, V> kp, kpp, kppl, kppr; ; ) {
      if ((kp = k.parent) == null) {
        k.red = false;
        return;
      } else if (!kp.red || (kpp = kp.parent) == null) {
        return;
      }
      if (kp == (kppl = kpp.left)) {
        if ((kppr = kpp.right) != null && kppr.red) {
          kppr.red = false;
          kp.red = false;
          kpp.red = true;
          k = kpp;
        } else {
          if (k == kp.right) {
            leftRotate((k = kp));
            kpp = (kp = k.parent) == null ? null : kp.parent;
          }
          if (kp != null) {
            kp.red = false;
            if (kpp != null) {
              kpp.red = true;
              rightRotate(kpp);
            }
          }
        }
      } else {
        if (kppl != null && kppl.red) {
          kppl.red = false;
          kp.red = false;
          kpp.red = true;
          k = kpp;
        } else {
          if (k == kp.left) {
            rightRotate((k = kp));
            kpp = (kp = k.parent) == null ? null : kp.parent;
          }
          if (kp != null) {
            kp.red = false;
            if (kpp != null) {
              kpp.red = true;
              leftRotate(kpp);
            }
          }
        }
      }
    }
  }

  /**
   * Cormem left rotate algorithm.
   *
   * @param x the rode to be rotated.
   */
  private void leftRotate(TreeNode<K, V> x) {
    TreeNode<K, V> r, pp, rl;
    if (x != null && (r = x.right) != null) {
      if ((rl = x.right = r.left) != null) rl.parent = x;
      if ((pp = r.parent = x.parent) == null) (root = r).red = false;
      else if (pp.left == x) pp.left = r;
      else pp.right = r;
      r.left = x;
      x.parent = r;
    }
  }
  /**
   * Cormem right rotate algorithm.
   *
   * @param x the rode to be rotated.
   */
  private void rightRotate(TreeNode<K, V> p) {
    TreeNode<K, V> l, pp, lr;
    if (p != null && (l = p.left) != null) {
      if ((lr = p.left = l.right) != null) lr.parent = p;
      if ((pp = l.parent = p.parent) == null) (root = l).red = false;
      else if (pp.right == p) pp.right = l;
      else pp.left = l;
      l.right = p;
      p.parent = l;
    }
  }

  /**
   * The insert node method.
   *
   * <p>Stores the values in a red-black tree node.
   *
   * @param hash the hash value of the entry.
   * @param key the key value of the entry.
   * @param value the value of the entry.
   * @return an old entry value if exists or {@code null} instead.
   */
  public V insert(int hash, K key, V value) {
    return putTreeVal(hash, key, value);
  }

  /**
   * Deletes a node from tree.
   *
   * <p>This method call a fixup delete to re-balance the tree.
   *
   * @param z the node to be removed.
   */
  private void deleteTreeNode(TreeNode<K, V> z) {
    if (z != null) {
      TreeNode<K, V> y = z, x, zl = z.left, zr = z.right;
      boolean oc = z.red;
      y = z;
      if (zl == null) {
        x = zr;
        rbTransplant(z, zr);
      } else if (zr == null) {
        rbTransplant(z, zl);
      } else {
        y = treeMin(zr);
        oc = y.red;
        x = y.right;
        if (y.parent == z) {
          x.parent = y;
        } else {
          rbTransplant(y, y.right);
          y.right = zr;
          y.right.parent = y;
        }
        rbTransplant(z, y);
        y.left = z.left;
        y.left.parent = y;
        y.red = z.red;
        if (!oc) {
          fixDelete(x);
        }
      }
    }
  }

  /**
   * This method replaces a subtree as a child of its parent with another subtree.
   *
   * <p>following the Cormen book.
   *
   * @param u the root of the first subtree
   * @param v the root of the second subtree
   */
  private void rbTransplant(TreeNode<K, V> u, TreeNode<K, V> v) {
    TreeNode<K, V> up;
    if ((up = u.parent) == null) {
      this.root = v;
    } else if (u != null && u == (up.left)) {
      up.left = v;
    } else {
      up.right = v;
    }
    v.parent = u.parent;
  }

  /**
   * This method returns the smallest node in a given tree.
   *
   * @param node the root of a tree.
   * @return the smallest node from the tree.
   */
  private TreeNode<K, V> treeMin(TreeNode<K, V> node) {
    TreeNode<K, V> cursor = node;
    while (cursor.left != null) {
      cursor = cursor.left;
    }
    return cursor;
  }

  /**
   * This method restores properties after deletion.
   *
   * @param x the node to be fixed.
   */
  private void fixDelete(TreeNode<K, V> x) {
    for (TreeNode<K, V> xp, xpl, xpr; ; ) {
      if (x == null || x == this.root) {
        return;
      } else if ((xp = x.parent) == null) {
        x.red = false;
      } else if (x.red) {
        x.red = false;
      } else if ((xpl = xp.left) == x) {
        if ((xpr = xp.right) != null && xpr.red) { // Case 1
          xpr.red = false;
          xp.red = true;
          leftRotate(xp);
          xpr = (xp = x.parent) == null ? null : xp.right;
        }
        if (xpr == null) x = xp;
        else {
          TreeNode<K, V> sl = xpr.left, sr = xpr.right;
          if ((sr == null || !sr.red) && (sl == null || !sl.red)) { // Case 2
            xpr.red = true;
            x = xp;
          } else {
            if (sr == null || !sr.red) { // case 3
              if (sl != null) sl.red = false;
              xpr.red = true;
              rightRotate(xpr);
              xpr = (xp = x.parent) == null ? null : xp.right;
            }
          }
        }
      } else { // symmetric
        if (xpl != null && xpl.red) {
          xpl.red = false;
          xp.red = true;
          rightRotate(xp);
          xpl = (xp = x.parent) == null ? null : xp.left;
        }
        if (xpl == null) x = xp;
        else {
          TreeNode<K, V> sl = xpl.left, sr = xpl.right;
          if ((sl == null || !sl.red) && (sr == null || !sr.red)) {
            xpl.red = true;
            x = xp;
          } else {
            if (sl == null || !sl.red) {
              if (sr != null) sr.red = false;
              xpl.red = true;
              leftRotate(xpl);
              xpl = (xp = x.parent) == null ? null : xp.left;
            }
          }
        }
      }
    }
  }

  /**
   * The remove node method.
   *
   * <p>Removes the values in a red-black tree node.
   *
   * @param node the node to be removed.
   */
  public void remove(TreeNode<K, V> node) {
    deleteTreeNode(node);
  }

  /**
   * Prints the tree from a given node.
   *
   * @param n the node.
   */
  public void preorder(TreeNode<K, V> n) {
    if (n != null) {
      System.out.println(n);
      preorder(n.left);
      preorder(n.right);
    }
  }
}

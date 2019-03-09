package lt.vu.fmi.javatech.store.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class StoreHashMap<K,V> implements Map<K, V>{

    private static final int INIT_SIZE = 1 << 4;
    
    private int size = 0;
    private Node<K,V>[] table = new Node[INIT_SIZE];
    
    private static int hash(Object o) {
        int h = o.hashCode();
        return (h >>> 16) ^ h;
    }
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private Node<K,V> getNode(Object key) {
        int hash = hash(key);
        int index = hash & (table.length - 1);
        
        Node<K,V> n = table[index];
        while (n != null) {
            if (n.key.equals(key)) {
                return n;
            }
            n = n.next;
        }
        return null;
    }
    
    @Override
    public boolean containsKey(Object key) {
        return getNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Node<K,V> n: table) {
            while (n != null) {
                if (n.value.equals(value)) {
                    return true;
                }
                n = n.next;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        Node<K, V> n = getNode(key);
        return n != null ? n.value : null;
    }

    @Override
    public V put(K key, V value) {
        int hash = hash(key);
        int index = hash & (table.length - 1);
        
        Node<K,V> n = table[index];
        if (n == null) {
            table[index] = new Node<>(key, value);
        } else {
            while (n != null) {
                if (n.key.equals(key)) {
                    return n.setValue(value);
                }
                if (n.next == null) {
                    n.next = new Node<>(key, value);
                    break;
                }
                n = n.next;
            }
        }
        size += 1;
        return null;
    }

    @Override
    public V remove(Object key) {
        int hash = hash(key);
        int index = hash & (table.length - 1);
        
        Node<K,V> n = table[index];
        if (n != null) {
            if (n.key.equals(key)) {
                table[index] = n.next;
                size -= 1;
                return n.value;
            }
            while (n.next != null) {
                if (n.next.key.equals(key)) {
                    V value = n.next.value;
                    n.next = n.next.next;
                    size -= 1;
                    return value;
                }
                n = n.next;
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<V> values() {
        return new AbstractCollection<V>() {
            
            @Override
            public Iterator<V> iterator() {
                return new Iterator<V>() {
                    
                    private final Iterator<Entry<K,V>> it = new NodeSet().iterator();
                    
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public V next() {
                        return it.next().getValue();
                    }
                    
                };
            }

            @Override
            public int size() {
                return size;
            }
            
        };
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new NodeSet();
    }
    
    class NodeSet extends AbstractSet<Entry<K,V>> {

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new Iterator<Entry<K, V>>() {
                
                private int index = 0;
                private Node<K,V> node;
                
                @Override
                public boolean hasNext() {
                    while (node == null && index < table.length) {
                        node = table[index++];
                    }
                    return node != null;
                }

                @Override
                public Entry<K, V> next() {
                    Node<K,V> curr = node;
                    node = node.next;
                    return curr;
                }
                
            };
        }

        @Override
        public int size() {
            return size;
        }
        
    }
    
    public class Node<K,V> implements Map.Entry<K,V> {

        K key;
        V value;
        Node<K,V> next;
        
        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
        
    }
    
}

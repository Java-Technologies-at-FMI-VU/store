package lt.vu.fmi.javatech.store.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class StoreHashMap<K,V> implements Map<K, V>, Serializable {

    private static final int INIT_SIZE = 1 << 4;
    
    private transient int size;
    private transient Node<K,V>[] table;
    
    private static int hash(Object o) {
        int h = o == null ? 0 : o.hashCode();
        return (h >>> 16) ^ h;
    }

    public StoreHashMap() {
        initialize();
    }
    
    private void initialize() {
        size = 0;
        table = new Node[INIT_SIZE];
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
            if ((key == null && n.key == null) || n.key.equals(key)) {
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
                if ((value == null && n.value == null) || n.value.equals(value)) {
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
            if ((key == null && n.key == null) || n.key.equals(key)) {
                table[index] = n.next;
                size -= 1;
                return n.value;
            }
            while (n.next != null) {
                if ((key == null && n.next.key == null) || n.next.key.equals(key)) {
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
        Iterator<? extends Entry<? extends K, ? extends V>> it = m.entrySet().iterator();
        while (it.hasNext()) {
            Entry<? extends K, ? extends V> e = it.next();
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        initialize();
    }

    @Override
    public Set<K> keySet() {
        return new AbstractSet<K>() {
            
            @Override
            public Iterator<K> iterator() {
                return new Iterator<K>() {
                    
                    private final Iterator<Entry<K, V>> it = new NodeSet().iterator();
                    
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public K next() {
                        return it.next().getKey();
                    }

                    @Override
                    public void remove() {
                        it.remove();
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
    
    final class NodeSet extends AbstractSet<Entry<K,V>> {

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new Iterator<Entry<K, V>>() {
                
                private int index = 0;
                private Node<K,V> node;
                private Node<K,V> prev;
                
                @Override
                public boolean hasNext() {
                    while (node == null && index < table.length) {
                        node = table[index++];
                    }
                    return node != null;
                }

                @Override
                public Entry<K, V> next() {
                    prev = node;
                    node = node.next;
                    return prev;
                }

                @Override
                public void remove() {
                    StoreHashMap.this.remove(prev.getKey());
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("StoreHashMap{");
        boolean isfirst = true;
        Iterator<Entry<K, V>> it = new NodeSet().iterator();
        while (it.hasNext()) {
            Entry<K, V> e = it.next();
            if (!isfirst) sb.append(",");
            sb.append(e.getKey()).append("=").append(e.getValue());
            isfirst = false;
        }
        return sb.append('}').toString();
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(size);
        for (Map.Entry<K,V> e: entrySet()) {
            out.writeObject(e.getKey());
            out.writeObject(e.getValue());
        }
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        initialize();
        int capacity = in.readInt();
        for (int i = 0; i < capacity; i++) {
            put((K) in.readObject(), (V) in.readObject());
        }
    }
    
    public static void main(String[] args) {
        
        Map m = new StoreHashMap();
        
        m.put(1, "vienas");
        m.put(2, "du");
        m.put("trys", 3);
        m.put(null, 3);
        m.put(4, null);
        
        System.out.println(m.containsKey(null));
        System.out.println(m.containsValue(null));
        
        System.out.println(m);
        
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                it.remove();
            }
        }
        
        System.out.println(m);
        
    }
    
}

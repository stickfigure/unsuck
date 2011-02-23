/*
 */

package unsuck.collections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Much like a LinkedHashMap, but has some unusual characteristics and a simplified
 * interface to make implementation easier.</p>
 * 
 * <p>First and foremost, this class has *very* efficient iteration over values.
 * It uses a linked list that plays fast and lose with thread safety; writes are
 * synchronized but iteration is not.  There is no check for comodifcation,
 * just a sort of "eventual consistency" on iteration.</p>
 * 
 * <p>This class will not work on all (or even most) JVMs and architectures.
 * In particular, it will probably fail on weakly-ordered architectures.  However,
 * x86 (including 64-bit) is strongly ordered so it should work well enough.
 * Another issue is the java compiler, which could reorder code in all sorts of
 * optimizations.  The bottom line is that this needs to be tested under load in your
 * specific environment before you can be sure it works.  The author is using
 * it successfully with OpenJDK + x86-64.</p>
 * 
 * <p>For a lot more info, google "memory barrier".</p>
 * 
 * <p>There is *no* guarantee on iteration order.</p>
 */
public class FastLooseMap<K, V> implements Iterable<V>
{
	/** */
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(FastLooseMap.class);

	/**
	 * The actual list nodes.
	 */
	static class Node<V>
	{
		V payload;
		Node<V> next;
		Node<V> previous;
		
		public String toString()
		{
			String prev = previous == null ? null : "present";
			String nxt = next == null ? null : "present";
			return this.getClass().getSimpleName() + "{previous=" + prev + ", next=" + nxt + ", payload=" + payload + "}";
		}
	}
	
	/**
	 * Implements the fast unsynchronized iterator across values
	 */
	class IteratorImpl implements Iterator<V>
	{
		Node<V> current;
		
		IteratorImpl(Node<V> node)
		{
			current = node;
		}

		@Override
		public boolean hasNext()
		{
			return current.next != null;
		}

		@Override
		public V next()
		{
			assert current.next != null;
			
			current = current.next;
			return current.payload;
		}

		@Override
		public void remove()
		{
			// Not hard to implement, but we would need to add the key to the Node
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * There is one bogus head node to make the algorithm easy.  The bogus node
	 * will start with previous and next pointing to itself, so the last node
	 * will always point to head (not null).
	 */
	Node<V> head;
	
	/**
	 * A map of key to node.  Doesn't need to be synchronized.
	 */
	Map<K, Node<V>> nodeMap = new HashMap<K, Node<V>>();
	
	/**
	 */
	public FastLooseMap()
	{
		this.clear();
	}
	
	/**
	 * Gets the value at the associated key, or null if there is none.
	 * Note that this method is synchronized and probably shouldn't be
	 * used in performance-sensitive places.  It must be synchronized
	 * because the nodemap isn't. 
	 */
	public synchronized V get(K key)
	{
		Node<V> node = this.nodeMap.get(key);
		if (node == null)
			return null;
		else
			return node.payload;
	}
	
	/**
	 * Adds or replaces the value.  Note that iteration order is never guaranteed.
	 */
	public synchronized void put(K key, V value)
	{
		Node<V> node = this.nodeMap.get(key);
		if (node == null)
		{
			node = new Node<V>();
			node.next = this.head.next;
			node.previous = this.head;
			this.head.next = node;
			
			this.nodeMap.put(key, node);
		}
		
		node.payload = value;
	}
	
	/**
	 * Remove the entry from the list
	 */
	public synchronized V remove(K key)
	{
		Node<V> node = this.nodeMap.get(key);
		if (node == null)
			return null;
		else
		{
			if (node.previous != null)
				node.previous.next = node.next;
			
			if (node.next != null)
				node.next.previous = node.previous;
			
			this.nodeMap.remove(key);
			
			return node.payload;
		}
	}
	
	/**
	 * Create a nice fast iterator on this list
	 */
	public Iterator<V> iterator()
	{
		return new IteratorImpl(this.head);
	}
	
	/**
	 */
	public synchronized void clear()
	{
		this.head = new Node<V>();

		this.nodeMap.clear();
	}

	/**
	 * @return the current number of elements in the list
	 */
	public int size()
	{
		return this.nodeMap.size();
	}

	/** A brief description */
	public String toString()
	{
		return this.getClass().getSimpleName() + "{size=" + size() + "}";
	}
}

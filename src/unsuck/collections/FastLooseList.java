/*
 */

package unsuck.collections;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>A linked list that plays fast and lose with thread safety requirements.
 * Writes are synchronized but iteration is not.  There is no check for comodifcation,
 * just a sort of "eventual consistency" on iteration.  Note that you can only
 * iterate forwards; the list is doubly linked only to facilitate remove().</p>
 * 
 * <p>This class is not guaranteed to work in all JVMs or with all compilers due
 * to memory caching and instruction reordering optimizations.  However, it
 * should work "well enough" in the Sun JVM on Intel.</p>
 * 
 * <p>To keep it simple and easy, this class does not implement List.</p>
 */
public class FastLooseList<T> implements Iterable<T>
{
	/** */
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(FastLooseList.class);

	/**
	 * The actual list nodes.
	 */
	static class Node<T>
	{
		T payload;
		Node<T> next;
		Node<T> previous;
	}
	
	/**
	 * Like an iterator but adds a replace() method
	 */
	public class Iter implements Iterator<T>
	{
		Node<T> current = head;

		@Override
		public boolean hasNext()
		{
			return current.next != head;
		}

		@Override
		public T next()
		{
			assert current.next != head;
			
			current = current.next;
			return current.payload;
		}

		@Override
		public void remove()
		{
			synchronized(FastLooseList.this)
			{
				current.previous.next = current.next;
				current.next.previous = current.previous;
				currentSize--;
			}
		}
		
		/** Replace the last element returned from next() with another object */
		public void set(T other)
		{
			assert current != head;
			
			// Doesn't need synchronization
			current.payload = other;
		}
	}

	/**
	 * Number of elements.
	 */
	int currentSize;
		
	/**
	 * There is one bogus head node to make the algorithm easy.  The bogus node
	 * will start with previous and next pointing to itself, so the last node
	 * will always point to head (not null).
	 */
	Node<T> head;
	
	/**
	 */
	public FastLooseList()
	{
		this.clear();
	}
	
	/**
	 * Inserts an entry at the front of the list
	 */
	public synchronized void insertFront(T entry)
	{
		Node<T> node = new Node<T>();
		node.next = this.head.next;
		node.previous = this.head;
		this.head.next = node;
		
		this.currentSize++;
	}
	
	/**
	 * Create a nice fast iterator on this list
	 */
	public Iter iterator()
	{
		return new Iter();
	}

	/**
	 */
	public synchronized void clear()
	{
		this.head = new Node<T>();
		this.head.next = this.head.previous = this.head;
		this.currentSize = 0;
	}

	/**
	 * @return the current number of elements in the list
	 */
	public int size()
	{
		return this.currentSize;
	}

}

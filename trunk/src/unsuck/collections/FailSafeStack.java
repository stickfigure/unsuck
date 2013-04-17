/*
 */

package unsuck.collections;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * A very simple Collection that provides fail-safe iterator behavior instead
 * of fail-fast iterator behavior. You can modify the structure of the list and
 * iterators will keep working.
 *
 * The implementation is a simple doubly-linked list with LIFO (ie, stack) behavior.
 * This has the characteristic that iterators in progress won't see added items.
 *
 * Warning: I'm not sure this works yet.
 *
 * @author Jeff Schnitzer
 */
public class FailSafeStack<E> extends AbstractCollection<E> {

	/** */
	private static class Node<E> {
		Node<E> prev;
		E item;
		Node<E> next;
		boolean removed;

		Node(Node<E> prev, E element, Node<E> next) {
			this.prev = prev;
			this.item = element;
			this.next = next;
		}

		public String toString() {
			String prevItem = prev == null ? null : System.identityHashCode(prev) + "(" + String.valueOf(prev.item) + ")";
			String nextItem = next == null ? null : System.identityHashCode(next) + "(" + String.valueOf(next.item) + ")";
			return this.getClass().getSimpleName() + "{prev=" + prevItem + ", item=" + item + ", next=" + nextItem + "}";
		}
	}

	/** */
	private class Itr implements Iterator<E> {
		private Node<E> here;

		public Itr() {
			here = head;
		}

		@Override
		public boolean hasNext() {
			return getNextNode() != null;
		}

		@Override
		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();

			here = getNextNode();
			return here.item;
		}

		/**
		 * This is slightly complicated because there is an ordering of removal that could result in
		 * a removed node still having a next pointer to another removed node.  In this case, just
		 * fast forward through them.
		 *
		 * @return the next non-removed node, or null if there is none
		 */
		private Node<E> getNextNode() {
			Node<E> foo = here.next;
			while (foo != null && foo.removed)
				foo = foo.next;

			return foo;
		}

		@Override
		public void remove() {
			if (here == head)
				throw new IllegalStateException("You must call next() at least once before calling remove()");

			// Just in case two separate iterators try to remove the same item
			if (here.removed)
				return;
			else
				here.removed = true;

			// Unlink from previous
			here.prev.next = here.next;

			// Unlink from next
			if (here.next != null)
				here.next.prev = here.prev;

			size--;

			assert size == calculateSizeTheHardWay();
		}

		/** Useful for our shuffle() implementation */
		public void set(E e) {
			here.item = e;
		}
	}

	/**
	 * Pointer to the head node, which always exists. The head node is a dummy node and the item is always null.
	 * New items are always added immediately after the head node (like a stack).
	 */
	final Node<E> head;

	/** Current size */
	int size;

	/** */
	public FailSafeStack() {
		head = new Node<E>(null, null, null);
	}

	/** Copyish constructor */
	public FailSafeStack(Collection<E> other) {
		this();
		addAll(other);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return size;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		return new Itr();
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e) {
		Node<E> node = new Node<E>(head, e, head.next);
		head.next = node;

		// Link in the backwards link
		if (node.next != null)
			node.next.prev = node;

		size++;

		assert size == calculateSizeTheHardWay();

		return true;
	}

	/** Useful for testing */
	private int calculateSizeTheHardWay() {
		int size = 0;
		Itr itr = new Itr();
		while (itr.hasNext()) {
			itr.next();
			size++;
		}

		return size;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#clear()
	 */
	@Override
	public void clear() {
		head.next = null;
		size = 0;
	}

	/**
	 * Shuffle the contents of the list as efficiently as possible.  Equivalent to Collections.shuffle(),
	 * which won't work here because we don't implement List.
	 */
	@SuppressWarnings("unchecked")
	public void shuffle(Random rnd) {
		if (size < 2)
			return;

		Object arr[] = this.toArray();
		assert arr.length == size;
		if (arr.length != size)
			throw new NullPointerException();

		// Shuffle array
		for (int i=size; i>1; i--)
			swap(arr, i-1, rnd.nextInt(i));

		// Dump array back into list
		Itr it = new Itr();
		for (int i=0; i<arr.length; i++) {
			it.next();
			it.set((E)arr[i]);
		}
	}

	/**
	 * Swaps the two specified elements in the specified array.
	 */
	private static void swap(Object[] arr, int i, int j) {
		Object tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}
}
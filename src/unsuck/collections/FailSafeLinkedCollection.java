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
 * This does not provide the full List interface - basically you can add items
 * to the end of the list and remove items from the iterator and that's it.
 * The implementation is a simple singly-linked list.
 *
 * @author Jeff Schnitzer
 */
public class FailSafeLinkedCollection<E> extends AbstractCollection<E> {

	/** */
	private static class Node<E> {
		E item;
		Node<E> next;	// always starts null

		Node(E element) {
			this.item = element;
		}
	}

	/** */
	private class Itr implements Iterator<E> {
		private Node<E> here;
		private Node<E> last;

		public Itr() {
			here = head;
		}

		@Override
		public boolean hasNext() {
			return here.next != null;
		}

		@Override
		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();

			last = here;
			here = here.next;
			return here.item;
		}

		@Override
		public void remove() {
			last.next = last.next.next;

			if (here == tail)
				tail = last;

			size--;
		}

		/** Useful for our shuffle() implementation */
		public void set(E e) {
			here.item = e;
		}
	}

	/**
	 * Pointer to the head node, which always exists. The head node is a dummy node and the item is always null.
	 */
	final Node<E> head;

	/**
	 * Pointer to the tail.  Unlike the head, this moves so we can always append to the end.
	 */
	Node<E> tail;

	/** Current size */
	int size;

	/** */
	public FailSafeLinkedCollection() {
		head = tail = new Node<E>(null);
	}

	/** Copyish constructor */
	public FailSafeLinkedCollection(Collection<E> other) {
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
		Node<E> node = new Node<E>(e);
		tail.next = node;
		tail = node;
		size++;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#clear()
	 */
	@Override
	public void clear() {
		head.next = null;
		tail = head;
		size = 0;
	}

	/**
	 * Shuffle the contents of the list as efficiently as possible.  Equivalent to Collections.shuffle(),
	 * which won't work here because we don't implement List.
	 */
	@SuppressWarnings("unchecked")
	public void shuffle(Random rnd) {
		if (size() < 2)
			return;

		Object arr[] = this.toArray();

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
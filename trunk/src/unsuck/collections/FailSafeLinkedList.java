/*
 */

package unsuck.collections;

import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A LinkedList implementation that provides fail-safe iterator behavior instead
 * of fail-fast iterator behavior. Unlike ConcurrentLinkedQueue, this is a
 * simple single threaded list. The difference is that you can modify the
 * structure of the list and iterators will keep working.
 *
 * Note that you cannot add items to the middle of a list via ListIterator.add();
 * this would create a potentially invalid situation if the current node or nodes
 * had been removed elsewhere.  It's always safe to add to the end of the list,
 * however.
 *
 * @author Jeff Schnitzer
 */
public class FailSafeLinkedList<E> extends AbstractSequentialList<E> {

	/** */
	private static class Node<E> {
		E item;
		Node<E> next;
		Node<E> prev;

		Node(Node<E> prev, E element, Node<E> next) {
			this.item = element;
			this.next = next;
			this.prev = prev;
		}
	}

	/** */
	private class ListItr implements ListIterator<E> {
		private Node<E> lastReturned;
		private Node<E> next;
		private int nextIndex;

		public ListItr(int index) {
			next = (index == size) ? null : node(index);
			nextIndex = index;
		}

		@Override
		public boolean hasNext() {
			return nextIndex < size;
		}

		@Override
		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();

			lastReturned = next;
			next = next.next;
			nextIndex++;
			return lastReturned.item;
		}

		@Override
		public boolean hasPrevious() {
			return nextIndex > 0;
		}

		@Override
		public E previous() {
			if (!hasPrevious())
				throw new NoSuchElementException();

			lastReturned = next = (next == null) ? last : next.prev;
			nextIndex--;
			return lastReturned.item;
		}

		@Override
		public int nextIndex() {
			return nextIndex;
		}

		@Override
		public int previousIndex() {
			return nextIndex - 1;
		}

		@Override
		public void remove() {
			if (lastReturned == null)
				throw new IllegalStateException();

			Node<E> lastNext = lastReturned.next;
			unlink(lastReturned);
			if (next == lastReturned)
				next = lastNext;
			else
				nextIndex--;
			lastReturned = null;
		}

		@Override
		public void set(E e) {
			if (lastReturned == null)
				throw new IllegalStateException();

			lastReturned.item = e;
		}

		@Override
		public void add(E e) {
			lastReturned = null;
			if (next == null)
				linkLast(e);
			else
				throw new UnsupportedOperationException("You can only add to the end");
			nextIndex++;
		}

	}

	/**
	 * Pointer to first node.
	 * Invariant: (first == null && last == null) ||
	 *            (first.prev == null && first.item != null)
	 */
	Node<E> first;

	/**
	 * Pointer to last node.
	 * Invariant: (first == null && last == null) ||
	 *            (last.next == null && last.item != null)
	 */
	Node<E> last;

	/** */
	int size;

	/** */
	@Override
	public ListIterator<E> listIterator(int index) {
		if (index < 0 || index >= size)
			throw new IllegalArgumentException(index + " is not a valid index");

		return new ListItr(index);
	}

	/** */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Links e as last element.
	 */
	void linkLast(E e) {
		final Node<E> l = last;
		final Node<E> newNode = new Node<E>(l, e, null);
		last = newNode;
		if (l == null)
			first = newNode;
		else
			l.next = newNode;
		size++;
	}

	/**
	 * Unlinks non-null node x.
	 */
	E unlink(Node<E> x) {
		final E element = x.item;
		final Node<E> next = x.next;
		final Node<E> prev = x.prev;

		if (prev == null) {
			first = next;
		} else {
			prev.next = next;
			x.prev = null;
		}

		if (next == null) {
			last = prev;
		} else {
			next.prev = prev;
			x.next = null;
		}

		x.item = null;
		size--;
		return element;
	}

	/**
	 * Returns the (non-null) Node at the specified element index.
	 */
	Node<E> node(int index) {
		if (index < (size >> 1)) {
			Node<E> x = first;
			for (int i = 0; i < index; i++)
				x = x.next;
			return x;
		} else {
			Node<E> x = last;
			for (int i = size - 1; i > index; i--)
				x = x.prev;
			return x;
		}
	}
}
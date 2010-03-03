/*
 * $Id$
 */

package unsuck.collections;

import java.util.AbstractSet;
import java.util.Iterator;


/**
 * A simple read-only set that always returns true for the contains() method.
 * 
 * @author Jeff Schnitzer
 */
public class ContainsEverythingSet<T> extends AbstractSet<T>
{
	static ContainsEverythingSet<?> INSTANCE = new ContainsEverythingSet<Object>();
	
	@SuppressWarnings("unchecked")
	public static <V> ContainsEverythingSet<V> instance() { return (ContainsEverythingSet<V>)INSTANCE; }
	
	@Override
	public boolean contains(Object o)
	{
		return true;
	}

	@Override
	public Iterator<T> iterator()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int size()
	{
		throw new UnsupportedOperationException();
	}
}
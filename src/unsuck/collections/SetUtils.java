/*
 * $Id$
 */

package unsuck.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * Some basic utilities for manipulating sets
 * 
 * @author Jeff Schnitzer
 */
public class SetUtils
{
	/**
	 * Constructs a union set of the collections, without the irritating unchecked warning
	 * of the varargs method.  Treats null parameters as empty collections.
	 */
	public static <T> Set<T> union(Collection<T> c1, Collection<T> c2)
	{
		Set<T> union = new HashSet<T>();
		
		if (c1 != null)
			union.addAll(c1);
		
		if (c2 != null)
			union.addAll(c2);
		
		return union;
	}
	
	/**
	 * Puts all the elements in a set
	 */
	public static <T> Set<T> asSet(T[] array)
	{
		if (array == null)
			return null;
		
		Set<T> result = new HashSet<T>(array.length * 2);
		for (T item: array)
			result.add(item);
		
		return result;
	}
	
	/**
	 * @return true if the collections have the same content, efficiently.  Checks null equality.
	 */
	public static <T> boolean contentsEqual(Collection<T> col, Set<T> set)
	{
		if (col == null)
			return set == null;
		else if (set == null)
			return false;
		else if (col.size() != set.size())
			return false;
		else
		{
			for (T obj: col)
				if (!set.contains(obj))
					return false;
			
			return true;
		}
	}
}
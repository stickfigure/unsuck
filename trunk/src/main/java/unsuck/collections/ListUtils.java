/*
 * $Id$
 */

package unsuck.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * Some basic utilities for manipulating lists
 * 
 * @author Jeff Schnitzer
 */
public class ListUtils
{
	/**
	 * Makes a new  list from the Iterable.  Might be as simple as casting. 
	 */
	public static <T> List<T> asList(Iterable<T> iterable)
	{
		if (iterable instanceof List<?>)
			return (List<T>)iterable;
		else
		{
			List<T> list = (iterable instanceof Collection<?>)
				? new ArrayList<T>(((Collection<?>)iterable).size())
				: new ArrayList<T>();
			
			for (T t: iterable)
				list.add(t);
			
			return list;
		}
	}
	
	/**
	 * Gets the index item out of an iterable collection just by iterating.  Not cheap, but handy.
	 */
	public static <T> T get(Iterable<T> coll, int index)
	{
		Iterator<T> it = coll.iterator();
		for (int i=0; i<index; i++)
			it.next();
		
		return it.next();
	}
}
/*
 * $Id$
 */

package unsuck.collections;

import java.util.ArrayList;
import java.util.Collection;
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
}
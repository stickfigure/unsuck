package unsuck.lang;

import java.util.Collection;



/**
 * Utility methods for comparison and list testing that are typesafe.  Gets around the annoying problem with
 * Object.equals(), Set.contains(), etc which doesn't give you compiler checking if you change the type of the
 * value passed in.
 */
public class Safe
{
	/**
	 * Null safe equality comparison with type safety
	 */
	public static <T> boolean equals(T o1, T o2) {
		return (o1 == o2) || ((o1 != null) && o1.equals(o2));
	}

	/**
	 * Collection.contains() but with type safety. 
	 */
	public static <T> boolean contains(Collection<T> coll, T element) {
		return coll.contains(element);
	}
	
	/**
	 * Collection.remove() but with type safety.
	 */
	public static <T> boolean remove(Collection<T> coll, T element) {
		return coll.remove(element);
	}
}
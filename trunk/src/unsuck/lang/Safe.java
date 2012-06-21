package unsuck.lang;

import java.util.Collection;
import java.util.Map;



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
	 * Adds and checks for null.
	 */
	public static <T> boolean addNotNull(Collection<T> coll, T element) {
		if (element == null)
			throw new NullPointerException();
		
		return coll.add(element);
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
	
	/**
	 * Map.remove() but with type safety.
	 */
	public static <K, V> V remove(Map<K, V> map, K key) {
		return map.remove(key);
	}
	
	/**
	 * Map.get() but with type safety.
	 */
	public static <K, V> V get(Map<K, V> map, K key) {
		return map.get(key);
	}
}
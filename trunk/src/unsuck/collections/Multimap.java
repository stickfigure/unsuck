/*
 * $Id$
 */

package unsuck.collections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Basic implementation of multimap that stores actual values
 * in a hashset.
 *
 * @author Jeff Schnitzer
 */
public class Multimap<K, V> extends HashMap<K, Set<V>>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Adds a value to the set associated with the key.
	 */
	public boolean add(K key, V value)
	{
		Set<V> set = this.get(key);
		if (set == null)
		{
			set = new HashSet<V>();
			this.put(key, set);
		}
		
		return set.add(value);
	}
	
	/**
	 * Creates a union of all the values for the specified keys.  A null
	 * keySet, like an empty keySet, will produce an empty set.
	 */
	public Set<V> union(Set<K> keySet)
	{
		Set<V> result = new HashSet<V>();
		
		if (keySet == null)
			return result;
		
		for (K key: keySet)
		{
			Set<V> values = this.get(key);
			if (values != null)
				result.addAll(values);
		}
		
		return result;
	}
	
	/**
	 * Creates a union of all the values.
	 */
	public Set<V> union()
	{
		Set<V> result = new HashSet<V>();
		
		for (Set<V> valueSet: this.values())
			result.addAll(valueSet);
		
		return result;
	}
}
/*
 */

package unsuck.collections;

import java.util.LinkedHashMap;

/**
 * <p>Linked hash map with LRU semantics.  Not synchronized.</p>
 */
public class LRUMap<K, V> extends LinkedHashMap<K, V>
{
	private static final long serialVersionUID = 1L;
	
	/** Max capacity */
	int max;
	
	/** */
	public LRUMap(int max)
	{
		super(max, 2f, true);	// arbitrarily large so we never rehash
	}

	/** */
	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest)
	{
		return this.size() > this.max;
	}
}

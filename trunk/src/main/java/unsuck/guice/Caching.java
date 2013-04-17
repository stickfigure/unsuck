package unsuck.guice;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Default implementation of Cacheable, just stores things in a map.</p>
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
public class Caching implements Cacheable
{
	private Map<Object, Object> cache; 

	@Override
	public void __setCache(Object key, Object value)
	{
		if (cache == null)
			cache = new HashMap<Object, Object>();
		
		cache.put(key, value);
	}

	@Override
	public Object __getCache(Object key)
	{
		return cache == null ? null : cache.get(key);
	}
}
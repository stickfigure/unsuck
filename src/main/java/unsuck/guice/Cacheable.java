package unsuck.guice;



/**
 * <p>Interface which objects should use if they want to use the @Cache annotation
 * and the CacheInterceptor.  Usually just extend Caching.</p>
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
public interface Cacheable
{
	void __setCache(Object key, Object value);
	Object __getCache(Object key);
}
package unsuck.guice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


/**
 * <p>Guice AOP interceptor which looks for {@code @Cache} annotation on methods and will permanently
 * cache the results.  Null results are cached as well.</p>
 * 
 * <p>Classes which are intercepted must implement the {@code Cacheable} interface.  Usually
 * just extend the {@code Caching} class.</p>
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
public class CacheInterceptor implements MethodInterceptor
{
	/** Sentinel value indicating a cached null result */
	private static Object NULL_RESULT = new Object();
	
	@Override
	public Object invoke(MethodInvocation inv) throws Throwable
	{
		Cacheable cacheable = (Cacheable)inv.getThis();
		
		Object key = inv.getMethod();
		Object cached = cacheable.__getCache(key);
		
		if (cached == null)
		{
			cached = inv.proceed();
			if (cached == null)
				cached = NULL_RESULT;
			
			cacheable.__setCache(key, cached);
		}
		
		if (cached == NULL_RESULT)
			return null;
		else
			return cached;
	}
}
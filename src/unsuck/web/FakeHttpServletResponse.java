/*
 */

package unsuck.web;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * HttpServletResponse which does nothing.
 * 
 * All methods will throw UnsupportedOperationException.
 */
public class FakeHttpServletResponse extends HttpServletResponseWrapper
{
	/** Create a stub interface via dynamic proxy that does nothing */
	private static HttpServletResponse makeStub()
	{
		return (HttpServletResponse)Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				new Class<?>[] { HttpServletResponse.class },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
					{
						throw new UnsupportedOperationException();
					}
				});
	}
	
	Map<String, Object> attrs = new HashMap<String, Object>();
	
	public FakeHttpServletResponse()
	{
		// Can't actually pass null here
		super(makeStub());
	}
}

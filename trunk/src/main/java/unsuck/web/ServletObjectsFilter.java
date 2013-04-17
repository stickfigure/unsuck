package unsuck.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p>Allows us to obtain servlet objects from a thread local.</p>
 * 
 * @author Jeff Schnitzer
 */
public class ServletObjectsFilter extends AbstractFilter
{
	static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();

	/** Gets the current request, or null if there is none */
	public static HttpServletRequest getRequest()
	{
		return requestThreadLocal.get();
	}

	/** Sets the relevant thread local */
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		requestThreadLocal.set(request);
		try
		{
			chain.doFilter(request, response);
		}
		finally
		{
			requestThreadLocal.set(null);
		}
	}
}
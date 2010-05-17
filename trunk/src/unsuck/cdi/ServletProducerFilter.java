package unsuck.cdi;

import java.io.IOException;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import unsuck.web.AbstractFilter;

/**
 * <p>Allows us to inject servlet objects and HTTP parameters.</p>
 * <p>See http://docs.jboss.org/weld/reference/latest/en-US/html/injection.html</p>
 * 
 * @author Jeff Schnitzer
 */
public class ServletProducerFilter extends AbstractFilter
{
	static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();
	
	/** Producer method which will give us a String value from a request */
	@Produces
	@HttpParam("")
	public static String getParamValue(InjectionPoint ip)
	{
		return getRequest().getParameter(ip.getAnnotated().getAnnotation(HttpParam.class).value());
	}
	
	/** */
	@Produces
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
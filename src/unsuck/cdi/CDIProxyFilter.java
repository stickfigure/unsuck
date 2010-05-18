package unsuck.cdi;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import unsuck.web.AbstractFilter;

/**
 * <p>Filter which proxies to another filter which was loaded using CDI and thus receives
 * proper dependency injection. You can use this on containers (like GAE) which do not support
 * CDI for Servlets.</p>
 * 
 * <p>Use an init-param which defines the actual class of the filter to load.</p>
 * 
 * @author Jeff Schnitzer
 */
public class CDIProxyFilter extends AbstractFilter
{
	private static final long serialVersionUID = 1L;
	
	/** */
	public static final String PROXY_FOR_INIT_PARAM_NAME = "realFilterClass";
	
	/** The actual, CDI-managed filter we wrap */
	Filter actual;

	/** */
	@Override
	public void init(FilterConfig cfg) throws ServletException
	{
		super.init(cfg);
		
		String className = cfg.getInitParameter(PROXY_FOR_INIT_PARAM_NAME);
		
		this.actual = (Filter)CDIUtils.getOrRegisterBean(cfg.getServletContext(), className);
		
		this.actual.init(cfg);
	}

	/** */
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException
	{
		this.actual.doFilter(request, response, chain);
	}

	@Override
	public void destroy()
	{
		this.actual.destroy();
	}
}

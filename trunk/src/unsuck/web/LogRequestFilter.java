/*
 * $Id$
 */

package unsuck.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Filter that logs all http requests as debug.
 * 
 * @author Jeff Schnitzer
 */
public class LogRequestFilter extends AbstractFilter
{
	/** */
	private final static Logger log = LoggerFactory.getLogger(LogRequestFilter.class);

	/** */
	@Override
	public void init(FilterConfig cfg) throws ServletException
	{
		super.init(cfg);
		
		log.info("Starting application");
	}
	
	/** */
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		logRequest(request);
		chain.doFilter(request, response);
	}
	
	/**
	 * Log the specified request.
	 */
	@SuppressWarnings("unchecked")
	public static void logRequest(HttpServletRequest request)
	{
		if (log.isDebugEnabled())
		{
			log.debug(request.getMethod());
			
			StringBuffer url = request.getRequestURL();
			
			String queryString = request.getQueryString();
			if (queryString != null)
			{
				url.append("?");
				url.append(queryString);
			}
			
			log.debug(url.toString());
			
			for (Map.Entry<String, String[]> param: ((Map<String, String[]>)request.getParameterMap()).entrySet())
				for (String val: param.getValue())
					log.debug("    " + param.getKey() + "=" + val);
			
			Enumeration<String> nameEnu = request.getHeaderNames();
			while (nameEnu.hasMoreElements())
			{
				String name = nameEnu.nextElement();
				Enumeration<String> valuesEnu = request.getHeaders(name);
				while (valuesEnu.hasMoreElements())
				{
					String value = valuesEnu.nextElement();
					log.debug("    " + name + ": " + value);
				}
			}
		}
	}
}
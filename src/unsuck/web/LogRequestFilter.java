/*
 * $Id$
 */

package unsuck.web;

import java.io.IOException;
import java.util.Enumeration;

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
	public void init(FilterConfig cfg)
	{
		super.init(cfg);
		
		log.info("Starting application");
	}
	
	/** */
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		if (log.isDebugEnabled())
		{
			StringBuffer url = request.getRequestURL();
			
			String queryString = request.getQueryString();
			if (queryString != null)
			{
				url.append("?");
				url.append(queryString);
			}
			
			log.debug(url.toString());
			
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
		
		chain.doFilter(request, response);
	}
}
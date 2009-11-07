/*
 * $Id$
 */

package unsuck.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import unsuck.web.AbstractFilter;


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
		}
		
		chain.doFilter(request, response);
	}
}
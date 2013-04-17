/*
 * $Id$
 */

package unsuck.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Any urls that get mapped to this filter will be forwarded to their .jsp equivalent.
 * Ie, /map -> /map.jsp.
 * 
 * Ignores any paths that end with / so that index.jsp can be used if this filter
 * is attached to a full directory.
 * 
 * It is a filter rather than a servlet so that if a dispatcher cannot be found, the
 * request is passed through so whatever normal 404 mechanism can take place.
 * 
 * @author Jeff Schnitzer
 */
public class PrettyURLFilter extends AbstractFilter
{
	/** */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(PrettyURLFilter.class);

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		String path = request.getServletPath();
		if (path.endsWith("/"))
		{
			chain.doFilter(request, response);
		}
		else
		{
			RequestDispatcher disp = request.getRequestDispatcher(path + ".jsp");
			if (disp == null)
				chain.doFilter(request, response);
			else
				disp.forward(request, response);
		}
	}
}
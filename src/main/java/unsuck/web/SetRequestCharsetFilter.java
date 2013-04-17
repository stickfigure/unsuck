/*
 * $Id$
 * $URL: https://subetha.googlecode.com/svn/trunk/src/org/subethamail/web/util/SetRequestCharsetFilter.java $
 */

package unsuck.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * For i18n reasons, we need to set the character encoding on
 * all requests into the server.
 */
public class SetRequestCharsetFilter extends AbstractFilter
{
	/**
	 */
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws IOException, ServletException
	{
		if (request.getCharacterEncoding() == null)
			request.setCharacterEncoding("UTF-8");

		chain.doFilter(request, response);
	}
}

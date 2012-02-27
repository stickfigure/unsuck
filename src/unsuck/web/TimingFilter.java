/*
 */

package unsuck.web;

import java.io.IOException;
import java.text.NumberFormat;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Just renders the amount of time it took to execute, in seconds
 */
public class TimingFilter extends AbstractFilter
{
	/** */
	private final static Logger log = LoggerFactory.getLogger(TimingFilter.class);
	
	/** */
	private final static NumberFormat FORMAT = NumberFormat.getNumberInstance();
	static {
		FORMAT.setMaximumFractionDigits(3);
	}
	
	/**
	 */
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		if (log.isDebugEnabled() && request.getAttribute("javax.servlet.forward.request_uri") == null) {
			long time = System.currentTimeMillis();
			
			chain.doFilter(request, response);
			
			long elapsed = System.currentTimeMillis() - time;
			float seconds = (float)elapsed / 1000f;
			log.debug(FORMAT.format(seconds) + " seconds");
		} else {
			chain.doFilter(request, response);
		}
	}
}

/*
 * $Id$
 */

package unsuck.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Filter that logs the contents of the response when debug is turned on for this
 * class.
 * 
 * @author Jeff Schnitzer
 */
public class LogResponseFilter extends AbstractFilter
{
	/** */
	private final static Logger log = LoggerFactory.getLogger(LogResponseFilter.class);
	
	/** */
	public static class OutputStreamWrapper extends ServletOutputStream
	{
		ServletOutputStream base;
		ByteArrayOutputStream buffer;
		
		public OutputStreamWrapper(ServletOutputStream base)
		{
			this.base = base;
			this.buffer = new ByteArrayOutputStream();
		}

		@Override
		public void close() throws IOException
		{
			this.base.close();
		}

		@Override
		public void flush() throws IOException
		{
			this.base.flush();
			
			String text = this.buffer.toString("utf-8");
			log.debug("Flushing: " + text);
			this.buffer.reset();
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException
		{
			this.base.write(b, off, len);
			this.buffer.write(b, off, len);
		}

		@Override
		public void write(int b) throws IOException
		{
			this.base.write(b);
			this.buffer.write(b);
		}
		
	}
	
	/** */
	public static class LoggingResponseWrapper extends HttpServletResponseWrapper
	{
		public LoggingResponseWrapper(HttpServletResponse response)
		{
			super(response);
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException
		{
			return new OutputStreamWrapper(super.getOutputStream());
		}
	}

	/** */
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		if (log.isDebugEnabled())
			chain.doFilter(request, new LoggingResponseWrapper(response));
		else
			chain.doFilter(request, response);
	}
}
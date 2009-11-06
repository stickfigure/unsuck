/*
 * $Id: SetRequestCharsetFilter.java 1120 2009-05-14 06:10:17Z lhoriman $
 * $URL: https://subetha.googlecode.com/svn/branches/resin/src/org/subethamail/web/util/SetRequestCharsetFilter.java $
 */

package unsuck.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import unsuck.io.BetterByteArrayOutputStream;

/**
 * <p>One of the fucked up things about the servlet API is that, on POST, you can
 * pretty much access *either* request.getParameter() *or* request.getInputStream().
 * JAX-RS systems consume the InputStream when processing @FormParam's, which
 * means you cannot use JAX-RS with filters that call getParameter().</p>
 * 
 * <p>This abomination of code, when installed as the first filter, makes it
 * possible to call both getParameter() and getInputStream() by buffering
 * the value in memory.  Don't use this filter when there might be very large
 * POST values.</p>
 * 
 * <p>This filter only works when the method is POST and the content type is
 * application/x-www-form-urlencoded.  Otherwise it just passes the request
 * through unmolested.</p>
 * 
 * <p>Also, this filter currently only buffers getInputStream(), not getReader().</p>
 * 
 * <p>This class depends on commons-io and commons-collections</p>
 * 
 * @author Jeff Schnitzer
 */
public class ProtectInputStreamFilter extends AbstractFilter
{
	/** */
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(ProtectInputStreamFilter.class);
	
	/** */
	protected static class ServletInputStreamWrapper extends ServletInputStream
	{
		InputStream base;
		
		public ServletInputStreamWrapper(InputStream base)
		{
			this.base = base;
		}

		@Override
		public int read() throws IOException
		{
			return this.base.read();
		}

		@Override
		public int available() throws IOException
		{
			return this.base.available();
		}

		@Override
		public void close() throws IOException
		{
			this.base.close();
		}

		@Override
		public synchronized void mark(int readlimit)
		{
			this.base.mark(readlimit);
		}

		@Override
		public boolean markSupported()
		{
			return this.base.markSupported();
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException
		{
			return this.base.read(b, off, len);
		}

		@Override
		public int read(byte[] b) throws IOException
		{
			return this.base.read(b);
		}

		@Override
		public void reset() throws IOException
		{
			this.base.reset();
		}

		@Override
		public long skip(long n) throws IOException
		{
			return this.base.skip(n);
		}	
	}
	
	/** */
	protected static class BufferingRequest extends HttpServletRequestWrapper
	{
		/** Call getParams() to lazy-populate this */
		private Map<String, List<String>> params;
		
		/** This is lazily converted from params */
		private Map<String, String[]> parameterMap;
		
		/** Holds the body for getInputStream() */
		BetterByteArrayOutputStream body;
		
		/** */
		public BufferingRequest(HttpServletRequest request)
		{
			super(request);
		}

		@Override
		public ServletInputStream getInputStream() throws IOException
		{
			if (this.body == null)
			{
				this.body = new BetterByteArrayOutputStream();
				IOUtils.copy(super.getInputStream(), this.body);
			}
			
			return new ServletInputStreamWrapper(this.body.getInputStream());
		}

		@Override
		public String getParameter(String name)
		{
			List<String> list = this.getParams().get(name);
			return (list == null) ? null : list.get(0);
		}

		@Override
		public Map<String, String[]> getParameterMap()
		{
			if (this.parameterMap == null)
			{
				this.parameterMap = new HashMap<String, String[]>((int)(this.getParams().size() * 0.8));
				
				for (Map.Entry<String, List<String>> entry: this.getParams().entrySet())
					this.parameterMap.put(entry.getKey(), entry.getValue().toArray(new String[entry.getValue().size()]));
				
				this.parameterMap = Collections.unmodifiableMap(this.parameterMap);
			}
			
			return this.parameterMap;
		}

		@Override
		@SuppressWarnings("unchecked")
		public Enumeration<String> getParameterNames()
		{
			return new IteratorEnumeration(this.getParams().keySet().iterator());
		}

		@Override
		public String[] getParameterValues(String name)
		{
			return this.getParameterMap().get(name);
		}

		@Override
		public BufferedReader getReader() throws IOException, IllegalStateException
		{
			throw new UnsupportedOperationException();
		}
		
		/**
		 * Builds up the parameter list from the input stream.
		 */
		private Map<String, List<String>> getParams()
		{
			if (this.params == null)
			{
				this.params = new HashMap<String, List<String>>();
				
				StringWriter bld = new StringWriter();
				
				try
				{
					InputStream in = this.getInputStream();
					int numChars = IOUtils.copy(new InputStreamReader(in, "utf-8"), bld);
					
					// One thing to watch out for, put the query string in here
					if (numChars > 0) bld.write('&');
					bld.write(this.getQueryString());
				}
				catch (IOException e)
				{
					throw new RuntimeException(e);
				}
					
				String query = bld.toString();
				
				String[] parts = query.split("&");
				for (String part: parts)
				{
					String[] keyVals = part.split("=");
					keyVals[0] = URLUtils.urlDecode(keyVals[0]);
					keyVals[1] = URLUtils.urlDecode(keyVals[1]);
					
					List<String> values = this.params.get(keyVals[0]);
					if (values == null)
					{
						values = new ArrayList<String>();
						this.params.put(keyVals[0], values);
					}
					
					values.add(keyVals[1]);
				}
				
				this.params = Collections.unmodifiableMap(this.params);
			}
			
			return this.params;
		}
	}
	
	/**
	 */
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws IOException, ServletException
	{
		if (request.getMethod().toUpperCase().equals("POST")
				&& (request.getContentType().equals("application/x-www-form-urlencoded")))
			chain.doFilter(new BufferingRequest(request), response);
		else
			chain.doFilter(request, response);
	}
}

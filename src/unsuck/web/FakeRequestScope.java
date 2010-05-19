/*
 * $Id: AbstractFilter.java 465 2006-05-22 01:30:41Z lhoriman $
 * $URL: https://subetha.googlecode.com/svn/branches/resin/src/org/subethamail/web/util/AbstractFilter.java $
 */

package unsuck.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * ServletRequest which implements only the attribute-related methods (and not even
 * all of those).  Suitable as a scope for framework bits that need such a thing.
 */
public class FakeRequestScope implements ServletRequest
{
	Map<String, Object> attrs = new HashMap<String, Object>();
	
	@Override
	public Object getAttribute(String key)
	{
		return attrs.get(key);
	}

	@Override
	public void setAttribute(String key, Object value)
	{
		attrs.put(key, value);
	}

	@Override
	public void removeAttribute(String key)
	{
		attrs.remove(key);
	}

	@Override
	public Enumeration<String> getAttributeNames()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public AsyncContext getAsyncContext()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCharacterEncoding()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getContentLength()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getContentType()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public DispatcherType getDispatcherType()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getLocalAddr()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getLocalName()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getLocalPort()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Locale getLocale()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<Locale> getLocales()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getParameter(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, String[]> getParameterMap()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<String> getParameterNames()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String[] getParameterValues(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getProtocol()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public BufferedReader getReader() throws IOException, IllegalStateException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRealPath(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRemoteAddr()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRemoteHost()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getRemotePort()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getScheme()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String getServerName()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getServerPort()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public ServletContext getServletContext()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isAsyncStarted()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isAsyncSupported()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSecure()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) throws IllegalStateException
	{
		throw new UnsupportedOperationException();
	}
}

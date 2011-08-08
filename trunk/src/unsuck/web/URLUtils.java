/*
 * $Id$
 */

package unsuck.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Some basic utilities for manipulating urls.
 *
 * @author Jeff Schnitzer
 */
public class URLUtils
{
	/**
	 * Stupid java doesn't come with a URL builder.
	 */
	public static String buildURL(String base, Map<String, Object> params)
	{
		StringBuilder bld = new StringBuilder();
		bld.append(base);

		if ((params != null) && !params.isEmpty())
		{
			boolean useAmpersand = false;
			for (Map.Entry<String, Object> entry: params.entrySet())
			{
				bld.append(useAmpersand ? "&" : "?");
				useAmpersand = true;

				bld.append(urlEncode(entry.getKey()));
				bld.append("=");
				bld.append(urlEncode(entry.getValue()));
			}
		}

		return bld.toString();
	}

	/**
	 * An interface to URLEncoder.encode() that isn't inane
	 */
	public static String urlEncode(Object value)
	{
		try
		{
			return URLEncoder.encode(value.toString(), "utf-8");
		}
		catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }
	}

	/**
	 * An interface to URLDecoder.decode() that isn't inane
	 */
	public static String urlDecode(Object value)
	{
		try
		{
			return URLDecoder.decode(value.toString(), "utf-8");
		}
		catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }
	}

	/**
	 * Generates a url from the request that is good for sending off with redirects
	 */
	public static String getActualUrl(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();

		String queryString = request.getQueryString();
		if (queryString != null)
		{
			url.append("?");
			url.append(queryString);
		}

		return url.toString();
	}
}
/*
 */

package unsuck.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequestWrapper;

/**
 * HttpServletRequest which implements only the attribute-related methods (and not even
 * all of those).  Suitable as a scope for framework bits that need such a thing.
 * 
 * All methods except attribute-related methods will throw NPE.
 */
public class FakeHttpServletRequest extends HttpServletRequestWrapper
{
	Map<String, Object> attrs = new HashMap<String, Object>();
	
	public FakeHttpServletRequest()
	{
		super(null);
	}

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
}

/*
 * $Id$
 */

package unsuck.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Some basic utilities for manipulating cookies.
 * 
 * @author Jeff Schnitzer
 */
public class CookieUtils
{
	/**
	 * @return null if cookie is not present
	 */
	public static Cookie getCookie(HttpServletRequest request, String name)
	{
		Cookie[] cookies = request.getCookies();
		
		if (cookies != null)
		{
			for (int i=0; i<cookies.length; i++)
			{
				if (cookies[i].getName().equals(name))
					return cookies[i];
			}
		}
		
		return null;
	}
	
	/**
	 * @return null if cookie is not present
	 */
	public static String getCookieValue(HttpServletRequest request, String name)
	{
		Cookie cook = getCookie(request, name);
		if (cook == null)
			return null;
		else
			return cook.getValue();
	}
	
	/**
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAge)
	{
		Cookie cook = new Cookie(name, value);
		cook.setMaxAge(maxAge);
		
		response.addCookie(cook);
	}
	
	/**
	 * Deletes the named cookie no matter what it's path or domain
	 */
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name)
	{
		Cookie cook = getCookie(request, name);
		if (cook != null)
		{
			cook.setMaxAge(0);
			response.addCookie(cook);
		}
	}
}
/*
 * $Id$
 */

package unsuck.json;

import org.codehaus.jackson.map.ObjectMapper;


/**
 * Some basic utilities for processing JSON.  This class depends on Jackson.
 * 
 * @author Jeff Schnitzer
 */
public class JSONUtils
{
	/**
	 * Converts the object to a JSON string
	 */
	public static String toJSON(Object value)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.writeValueAsString(value);
		}
		catch (RuntimeException ex) { throw ex; }
		catch (Exception ex) { throw new RuntimeException(ex); }
	}
	
	/**
	 * Converts the JSON string to an Object (either Map or List)
	 */
	public static Object fromJSON(String value)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.readValue(value, Object.class);
		}
		catch (RuntimeException ex) { throw ex; }
		catch (Exception ex) { throw new RuntimeException(ex); }
	}
}
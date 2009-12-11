/*
 * $Id$
 */

package unsuck.json;

import java.io.StringWriter;

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
			StringWriter writer = new StringWriter();
			
			mapper.writeValue(writer, value);
			
			return writer.toString();
			
			// This requires Jackson 1.3 which breaks GAE.
			// TODO, replace with this code when bug is fixed:
			// http://jira.codehaus.org/browse/JACKSON-188
			//return mapper.writeValueAsString(value);
		}
		catch (RuntimeException ex) { throw ex; }
		catch (Exception ex) { throw new RuntimeException(ex); }
	}
	
	/**
	 * Converts the JSON string to an Object (either Map or List)
	 */
	public static Object fromJSON(String value)
	{
		return fromJSON(value, Object.class);
	}
	
	/**
	 * Converts the JSON string to a typed object (or Map/List if Object.class is passed in)
	 */
	public static <T> T fromJSON(String value, Class<T> type)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.readValue(value, type);
		}
		catch (RuntimeException ex) { throw ex; }
		catch (Exception ex) { throw new RuntimeException(ex); }
	}
}
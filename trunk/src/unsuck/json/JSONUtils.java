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
		StringWriter json = new StringWriter();
		
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			mapper.writeValue(json, value);
		}
		catch (RuntimeException ex) { throw ex; }
		catch (Exception ex) { throw new RuntimeException(ex); }
		
		return json.toString();
	}
}
/*
 */

package unsuck.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


/**
 * Cleans up some of the annoying checked exceptions
 *
 * @author Jeff Schnitzer
 */
public class BetterObjectMapper extends ObjectMapper
{
	/**
	 * Converts the object to a JSON string
	 */
	public String toJSON(Object value)
	{
		try
		{
			return this.writeValueAsString(value);
		}
		catch (RuntimeException ex) { throw ex; }
		catch (Exception ex) { throw new RuntimeException(ex); }
	}

	/**
	 * Converts the JSON string to a typed object via a TypeReference
	 * The main complication is handling of Generic types: if they are used, one
	 * has to use TypeReference object, to work around Java Type Erasure.
	 *
	 * ex: return mapper.fromJSON(this.answersJson, new TypeReference<List<StanzaAnswer>>(){});
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromJSON(String value, TypeReference<T> type)
	{
		try
		{
			return (T)this.readValue(value, type);
		}
		catch (RuntimeException ex) { throw ex; }
		catch (Exception ex) { throw new RuntimeException(ex); }
	}

	/**
	 * Converts the JSON string to a typed object (or Map/List if Object.class is passed in)
	 */
	public <T> T fromJSON(String value, Class<T> type)
	{
		try
		{
			return this.readValue(value, type);
		}
		catch (RuntimeException ex) { throw ex; }
		catch (Exception ex) { throw new RuntimeException(ex); }
	}

	
}
/*
 */

package unsuck.hessian;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.MapDeserializer;

/**
 * Caucho's hessian implementation deserializes throwables by instantiating an object and
 * populating its fields.  This doesn't work on appengine because you can't write to private
 * fields of system classes.  This deserializer is quite a bit smarter.
 */
public class StackTraceElementDeserializer extends AbstractDeserializer
{
	private static final Logger log = Logger.getLogger(StackTraceElementDeserializer.class.getName());
	
	/** This thing knows how to convert to a basic HashMap which we can use */
	MapDeserializer mapper = new MapDeserializer(HashMap.class);

	/** */
	public StackTraceElementDeserializer()
	{
		log.fine("Constructed stack trace element deserializer");
	}

	/** Always produces StackTraceElement */
	@Override
	public Class<?> getType()
	{
		return StackTraceElement.class;
	}

	@Override
	public Object readMap(AbstractHessianInput in) throws IOException
	{
		@SuppressWarnings("unchecked")
		Map<String, Object> stuff = (Map<String, Object>)this.mapper.readMap(in);

		return this.convert(stuff);
	}

//	@Override
//	public Object readObject(AbstractHessianInput in, String[] fieldNames) throws IOException
//	{
//		@SuppressWarnings("unchecked")
//		Map<String, Object> stuff = (Map<String, Object>)this.mapper.readObject(in, fieldNames);
//
//		return this.convert(stuff);
//	}
	
	@Override
	public Object readObject(AbstractHessianInput in, Object[] fields) throws IOException
	{
		@SuppressWarnings("unchecked")
		Map<String, Object> stuff = (Map<String, Object>)this.mapper.readObject(in, fields);

		return this.convert(stuff);
	}

	/** Converts a hashmap of values to a StackTraceElement */
	private StackTraceElement convert(Map<String, Object> fields)
	{
		return new StackTraceElement(
				(String)fields.get("declaringClass"),
				(String)fields.get("methodName"),
				(String)fields.get("fileName"),
				(Integer)fields.get("lineNumber"));
	}
}

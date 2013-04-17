/*
 */

package unsuck.hessian;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.MapDeserializer;

/**
 * Caucho's hessian implementation deserializes throwables by instantiating an object and
 * populating its fields.  This doesn't work on appengine because you can't write to private
 * fields of system classes.  This deserializer is quite a bit smarter.
 */
public class ThrowableDeserializer extends AbstractDeserializer
{
	//private static final Logger log = Logger.getLogger(ThrowableDeserializer.class.getName());

	/** This thing knows how to convert to a basic HashMap which we can use */
	MapDeserializer mapper = new MapDeserializer(HashMap.class);
	
	/** */
	Class<?> cls;

	/** */
	public ThrowableDeserializer(Class<?> cls)
	{
		this.cls = cls;
	}

	/** */
	@Override
	public Class<?> getType()
	{
		return this.cls;
	}

	@Override
	public Object readMap(AbstractHessianInput in) throws IOException
	{
		@SuppressWarnings("unchecked")
		Map<String, Object> stuff = (Map<String, Object>)this.mapper.readMap(in);

		return this.convert(stuff);
	}

	@Override
	public Object readObject(AbstractHessianInput in, Object[] fields) throws IOException
	{
		@SuppressWarnings("unchecked")
		Map<String, Object> stuff = (Map<String, Object>)this.mapper.readObject(in, fields);

		return this.convert(stuff);
	}

	/** Converts a hashmap of values to a StackTraceElement */
	private Throwable convert(Map<String, Object> fields) throws IOException
	{
		String detailMessage = (String)fields.get("detailMessage");
		StackTraceElement[] stackTrace = (StackTraceElement[])fields.get("stackTrace");
		
		// Uninitialized causes are a pointer to the throwable's /this/, which becomes the map
		Object maybeCause = fields.get("cause");
		Throwable cause = (maybeCause == fields) ? null : (Throwable)maybeCause;
		
		Throwable throwable = null;

		// We look for a constructor in the following order:
		// 1) Thing(detailMessage, cause)
		// 2) Thing(detailMessage)
		// 3) Thing()
		// 4) TODO:  grab one at random and pass in nulls/zeroes/false
		try
		{
		    try
			{
				Constructor<?> ctor = this.cls.getConstructor(String.class, Throwable.class);
				throwable = (Throwable)ctor.newInstance(detailMessage, cause);
			}
			catch (NoSuchMethodException e)
			{
				try
				{
					Constructor<?> ctor = this.cls.getConstructor(String.class);
					throwable = (Throwable)ctor.newInstance(detailMessage);
				}
				catch (NoSuchMethodException e2)
				{
					Constructor<?> ctor = this.cls.getConstructor();
					throwable = (Throwable)ctor.newInstance();
				}
			}
		}
		catch (Exception ex)
		{
			throw new HessianProtocolException("'" + this.cls.getName() + "' could not be instantiated", ex);
		}

		if (stackTrace != null)
			throwable.setStackTrace(stackTrace);
		
		// TODO: populate any fields which are not from the java or javax domains.
		
		return throwable;
	}
}

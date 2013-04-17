/*
 */

package unsuck.hessian;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;

/**
 * Caucho's hessian implementation deserializes throwables by instantiating an object and
 * populating its fields.  This doesn't work on appengine because you can't write to private
 * fields of system classes.
 * 
 * This serializer factory creates deserializers smart enough to work around the problem.
 */
public class ThrowableSerializerFactory extends AbstractSerializerFactory
{
	@Override
	@SuppressWarnings("rawtypes")
	public Serializer getSerializer(Class paramClass) throws HessianProtocolException
	{
		return null;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Deserializer getDeserializer(Class paramClass) throws HessianProtocolException
	{
		if (Throwable.class.isAssignableFrom(paramClass))
		{
			return new ThrowableDeserializer(paramClass);
		}
		else if (paramClass == StackTraceElement.class)
		{
			return new StackTraceElementDeserializer();
		}
		else
			return null;
	}

}

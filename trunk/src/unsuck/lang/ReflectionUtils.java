package unsuck.lang;

import java.lang.reflect.Field;



/**
 * Some basic stupid java tools.
 */
public class ReflectionUtils
{
	/** Checked exceptions are LAME. */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> classForName(String name)
	{
		try
		{
			return (Class<T>)Class.forName(name);
		}
		catch (ClassNotFoundException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	/** Checked exceptions are LAME. */
	public static Object fieldGet(Field field, Object obj)
	{
		try
		{
			return field.get(obj);
		}
		catch (IllegalArgumentException e) { throw new RuntimeException(e); }
		catch (IllegalAccessException e) { throw new RuntimeException(e); }
	}

	/** Checked exceptions are LAME. */
	public static void fieldSet(Field field, Object obj, Object value)
	{
		try
		{
			field.set(obj, value);
		}
		catch (IllegalArgumentException e) { throw new RuntimeException(e); }
		catch (IllegalAccessException e) { throw new RuntimeException(e); }
	}

	/** Checked exceptions are LAME. */
	public static Field getField(Class<?> clazz, String fieldName) {
		try
		{
			return clazz.getField(fieldName);
		}
		catch (SecurityException ex) { throw new RuntimeException(ex); }
		catch (NoSuchFieldException ex) { throw new RuntimeException(ex); }
	}

	/** Checked exceptions are LAME. */
	public static Object getStaticFieldValue(Class<?> clazz, String fieldName) {
		return fieldGet(getField(clazz, fieldName), null);
	}

	/** Checked exceptions are LAME. */
	public static Object getFieldValue(Object obj, String fieldName) {
		return fieldGet(getField(obj.getClass(), fieldName), obj);
	}
	
	/**
	 * Invokes a simple method on the object, no parameters.
	 * No checked exceptions.  LAME.
	 */
	public static Object invokeMethod(Object obj, String method) {
		try
		{
			return obj.getClass().getMethod(method).invoke(obj);
		}
		catch (RuntimeException ex) { throw ex; }
		catch (Exception ex) { throw new RuntimeException(ex); }
	}
}
package unsuck.lang;



/**
 * Some basic stupid java tools.
 */
public class ReflectionUtils
{
	/**
	 * Converts the nasty checked exceptions to runtime exceptions
	 */
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
}
package unsuck.lang;


/**
 * Some basic stupid java tools.
 */
public class StringUtils
{
	/**
	 * Throws exception if value is null or empty or whitespace
	 */
	public static void requireNotBlank(String value)
	{
		if (value == null || value.trim().length() == 0)
			throw new IllegalArgumentException("Value cannot be null");
	}
}
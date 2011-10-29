package unsuck.lang;



/**
 * Some basic stupid java tools.
 */
public class Utils
{
	/**
	 * Null safe equality comparison
	 */
	public static boolean safeEquals(Object o1, Object o2)
	{
		return (o1 == o2) || ((o1 != null) && o1.equals(o2));
	}

	/** Compare two longs */
	public static int compare(long l1, long l2) {
		return (l1 == l2) ? 0 : (l1 < l2) ? -1 : 1;
	}
}
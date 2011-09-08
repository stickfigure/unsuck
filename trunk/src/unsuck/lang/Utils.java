package unsuck.lang;

import java.util.Arrays;
import java.util.Set;


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

	/**
	 * Throws exception if value is null or empty or whitespace
	 */
	public static void requireNotBlank(String value)
	{
		if ((value == null) || (value.trim().length() == 0))
			throw new IllegalArgumentException("Value cannot be null");
	}

	/**
	 * Splits name into words. Normalizes to lower case.
	 */
	public static void breakdown(String name, Set<String> words)
	{
		String[] tokens = name.toLowerCase().split(" ");
		words.addAll(Arrays.asList(tokens));
	}
	
	/**
	 * Takes a normal string and turns it into something suitable for a title in a URL.
	 * This is all about SEO.  Basically, spaces go to dash and anything that isn't
	 * URL-friendly gets stripped out.
	 */
	public static String makeTitle(String title)
	{
		if (title == null)
			return "";
		
		StringBuilder bld = new StringBuilder();
		
		for (int i=0; i<title.length(); i++)
		{
			char ch = title.charAt(i);
			
			if (Character.isWhitespace(ch))
				bld.append('-');
			else if (Character.isLetterOrDigit(ch))
				bld.append(ch);
			
			// otherwise skip
		}
		
		return bld.toString();
	}
}
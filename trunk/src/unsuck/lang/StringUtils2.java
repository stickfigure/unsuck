package unsuck.lang;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * Some tools for working with strings.  Named so as not to conflict with apache commons StringUtils.
 */
public class StringUtils2
{
	/**
	 * Returns a trimmed string or null if trimming produces an empty string.  Also produces null if input is null.
	 */
	public static String trimToNull(String value) {
		if (value == null)
			return null;
		
		value = value.trim();
		
		if (value.isEmpty())
			return null;
		else
			return value;
	}
	
	/**
	 * Throws exception if value is null or empty or whitespace.
	 * @return the trimmed, safe version of the string
	 */
	public static String requireNotBlank(String value)
	{
		if (value == null)
			throw new IllegalArgumentException("Value cannot be null");

		value = value.trim();

		if (value.length() == 0)
			throw new IllegalArgumentException("Value cannot be blank");

		return value;
	}

	/**
	 * Splits name into words. Normalizes to lower case.
	 */
	public static Set<String> breakdownWords(String name)
	{
		String[] tokens = name.toLowerCase().split(" ");
		return new HashSet<String>(Arrays.asList(tokens));
	}

	/**
	 * Splits name into words, and then fragments. Normalizes to lower case.
	 * For example, the string "Foo Bar" would become:
	 * "f", "fo", "foo", "b", "ba", "bar"
	 */
	public static Set<String> breakdownFragments(String name)
	{
		Set<String> into = new HashSet<String>(name.length() * 2);

		String[] tokens = name.toLowerCase().split(" ");
		for (String token: tokens)
			for (int i=1; i<=token.length(); i++)
				into.add(token.substring(0, i));

		return into;
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

		// Strip out any extra -'s that might get generated
		String dedup = bld.toString().replaceAll("-+", "-");
		if (dedup.charAt(dedup.length() - 1) == '-') {
			return dedup.substring(0, dedup.length() - 1);
		}
		return dedup;
	}

	/**
	 * Without the stupid exception
	 */
	public static byte[] getBytes(String str, String encoding)
	{
		try { return str.getBytes(encoding); }
		catch (UnsupportedEncodingException ex) { throw new RuntimeException(ex); }
	}

	/**
	 * Without the stupid exception
	 */
	public static byte[] getBytesUTF8(String str)
	{
		return getBytes(str, "UTF-8");
	}

	/**
	 * Without the stupid exception
	 */
	public static String newString(byte[] bytes, String encoding)
	{
		try { return new String(bytes, encoding); }
		catch (UnsupportedEncodingException ex) { throw new RuntimeException(ex); }
	}

	/**
	 * Without the stupid exception
	 */
	public static String newStringUTF8(byte[] bytes)
	{
		return newString(bytes, "UTF-8");
	}

}
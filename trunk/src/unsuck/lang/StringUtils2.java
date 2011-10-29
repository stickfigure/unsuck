package unsuck.lang;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Set;


/**
 * Some tools for working with strings.  Named so as not to conflict with apache commons StringUtils.
 */
public class StringUtils2
{
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
	public static void breakdownWords(String name, Set<String> into)
	{
		String[] tokens = name.toLowerCase().split(" ");
		into.addAll(Arrays.asList(tokens));
	}
	
	/**
	 * Splits name into words, and then fragments. Normalizes to lower case.
	 * For example, the string "Foo Bar" would become:
	 * "f", "fo", "foo", "b", "ba", "bar"
	 */
	public static void breakdownFragments(String name, Set<String> into)
	{
		String[] tokens = name.toLowerCase().split(" ");
		for (String token: tokens)
			for (int i=0; i<token.length(); i++)
				into.add(token.substring(0, i));
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
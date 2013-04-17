/*
 * $Id: TextTool.java 1075 2009-05-07 06:41:19Z lhoriman $
 * $URL: https://subetha.googlecode.com/svn/branches/resin/src/org/subethamail/web/util/TextTool.java $
 */

package unsuck.web;

/**
 * Some simple static methods useful for dealing with text on the web.
 *
 * @author Jeff Schnitzer
 */
public class TextUtils
{
	/**
	 * Escapes all xml characters, but also converts newlines to br tags.
	 */
	public static String escapeText(String orig)
	{
		StringBuilder buf = new StringBuilder();

		for (int i=0; i<orig.length(); i++)
		{
			char c = orig.charAt(i);

			switch(c)
			{
				case '>': buf.append("&gt;"); break;
				case '<': buf.append("&lt;"); break;
				case '\'': buf.append("&apos;"); break;
				case '"': buf.append("&quot;"); break;
				case '&': buf.append("&amp;"); break;
				case '\n': buf.append("<br />"); break;
				default: buf.append(c);
			}
		}

		return buf.toString();
	}

	/**
	 * Escapes all xml characters, WITHOUT converting newlines to br tags.
	 */
	public static String escapeXML(String orig)
	{
		StringBuilder buf = new StringBuilder();

		for (int i=0; i<orig.length(); i++)
		{
			char c = orig.charAt(i);

			switch(c)
			{
				case '>': buf.append("&gt;"); break;
				case '<': buf.append("&lt;"); break;
				case '\'': buf.append("&apos;"); break;
				case '"': buf.append("&quot;"); break;
				case '&': buf.append("&amp;"); break;
				default: buf.append(c);
			}
		}

		return buf.toString();
	}
}

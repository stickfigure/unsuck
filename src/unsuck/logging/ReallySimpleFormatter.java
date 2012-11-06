/*
 */

package unsuck.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * A formatter that makes it easier to read log messages.
 * From https://code.google.com/p/unsuck/
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
public class ReallySimpleFormatter extends SimpleFormatter
{
	static final int MESSAGE_COLUMN = 60;
	
	@Override
	public synchronized String format(LogRecord record)
	{
		StringBuilder bld = new StringBuilder();
		
		// Log level, pad to spacing for longest level (WARNING)
		String longest = "       ";
		String level = record.getLevel().toString();
		int diff = longest.length() - level.length(); 
		if (diff > 0)
			level = longest.substring(0, diff) + level;
		
		bld.append(level).append(":  ");
		
		// Class.method using shortName
		String className = record.getSourceClassName();
		className = className.substring(className.lastIndexOf('.')+1);
		bld.append(className).append('.').append(record.getSourceMethodName()).append(":  ");
		
		if (bld.length() < MESSAGE_COLUMN)
			while (bld.length() < MESSAGE_COLUMN)
				bld.append(' ');
		else
			bld.append('\t');
		
		// The message
		bld.append(record.getMessage());

		// Newline
		bld.append('\n');

		// If there is an exception, add it
		if (record.getThrown() != null) {
			try {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				record.getThrown().printStackTrace(pw);
				pw.close();
				bld.append(sw.toString());
			} catch (Exception ex) {
			}
		}
		
		return bld.toString();
	}
}

package unsuck.lang;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;


/**
 * A simple tool that lets you add a bunch of strings and then convert them to a separated list.
 * If no flags, result is empty string.
 */
public class FlagsString
{
	List<String> flags;
	
	public void add(Object flag) {
		if (flags == null)
			flags = Lists.newArrayList();
		
		flags.add(flag.toString());
	}
	
	public String toString() {
		if (flags == null)
			return "";
		else
			return StringUtils.join(flags, "; ");
	}
}
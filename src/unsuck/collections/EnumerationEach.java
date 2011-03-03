/*
 * $Id$
 */

package unsuck.collections;

import java.util.Enumeration;

/**
 * Performs some operation on each member of an enumeration
 * 
 * @author Jeff Schnitzer
 */
abstract public class EnumerationEach<T>
{
	public EnumerationEach(Enumeration<T> enu)
	{
		while (enu.hasMoreElements())
			this.process(enu.nextElement());
	}
	
	/** */
	abstract public void process(T thing);
}
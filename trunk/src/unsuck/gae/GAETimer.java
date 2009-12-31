/*
 * $Id: Geometry.java,v 1.2 2003/09/30 06:05:13 jeff Exp $
 * $Source: /cvsroot/Similarity4/src/java/com/similarity/util/Geometry.java,v $
 */

package unsuck.gae;

/**
 * Simple timer that lets us know when we're nearing the end of google app engine's
 * time limitations.
 *
 * @author Jeff Schnitzer
 */
public class GAETimer
{
	/** Total timeout before GAE aborts */
	public static final long TIMEOUT_MILLIS = 1000 * 30;
	
	/** Amount of leeway to maintain */
	public static final long LEEWAY_MILLIS = 1000 * 7;
	
	/** */
	public static final long DANGER_MILLIS = TIMEOUT_MILLIS - LEEWAY_MILLIS;

	/** When we started */
	long startTime;
	
	/** */
	public GAETimer()
	{
		this.startTime = System.currentTimeMillis();
	}

	/** @return true if the time is nearing the deadline */
	public boolean isNearDeadline()
	{
		return System.currentTimeMillis() - this.startTime > DANGER_MILLIS;
	}
}



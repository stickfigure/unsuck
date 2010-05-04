/*
 * $Id: Geometry.java,v 1.2 2003/09/30 06:05:13 jeff Exp $
 * $Source: /cvsroot/Similarity4/src/java/com/similarity/util/Geometry.java,v $
 */

package unsuck.gae;

import java.sql.Time;

/**
 * Simple timer that lets us know when we're nearing the end of google app engine's
 * time limitations.
 *
 * @author Jeff Schnitzer
 * @author Scott Hernandez
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
	
	/** The number of millis to start warning at. */
	long dangerMillis = DANGER_MILLIS;
	
	/** */
	public GAETimer()
	{
		this.startTime = System.currentTimeMillis();
	}

	public GAETimer(long leeway)
	{
		this();
		this.dangerMillis = TIMEOUT_MILLIS - leeway;
	}

	/** @return true if the time is nearing the deadline */
	public boolean isNearDeadline()
	{
		return getElapsedMillis() > dangerMillis;
	}
	
	/** @return startTime minus now (in millis) */
	public long getElapsedMillis()
	{
		return System.currentTimeMillis() - this.startTime;
	}
}
/*
 * $Id: Geometry.java,v 1.2 2003/09/30 06:05:13 jeff Exp $
 * $Source: /cvsroot/Similarity4/src/java/com/similarity/util/Geometry.java,v $
 */

package unsuck.geo;

/**
 * Provides static methods useful for comparing things like distances.
 *
 * @author Jeff Schnitzer
 */
public class Geometry
{
	/**
	 * Mean radius in Km
	 */
	protected static final double EARTH_RADIUS = 6371.0;

	/**
	 * Calculates the distance between two latitude, longitude values
	 * using a great circle computation.  Note that the algoritm assumes
	 * the earth to be a perfect sphere, whereas in fact the equatorial
	 * radius is about 30Km greater than the polar.
	 *
	 * All parameters must be in radians.
	 *
	 * @return the distance in Kilometers.
	 *
	 * @author http://www.mercury.demon.co.uk/dist/formula.html
	 */
	public static double calculateDistance(double latA, double lonA, double latB, double lonB)
	{
		double p1 = Math.cos(latA) * Math.cos(lonA) * Math.cos(latB) * Math.cos(lonB);
		
		double p2 = Math.cos(latA) * Math.sin(lonA) * Math.cos(latB) * Math.sin(lonB);
		
		double p3 = Math.sin(latA) * Math.sin(latB);
		
		return Math.acos(p1 + p2 + p3) * EARTH_RADIUS;
	}
}



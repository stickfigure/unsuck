/*
 */

package unsuck.lang;

import java.sql.Date;
import java.util.Calendar;

/**
 * Some tools for working with dates.
 *  
 * @author Jeff Schnitzer
 */
public class DateUtil
{
	/**
	 */
	protected static final long MILLIS_IN_DAY = 1000L * 60L * 60L * 24L;
	
	/**
	 * Figures out the cutoff birthdate for someone with the specified age.
	 * This is the oldest date that a birthday can be for the person to be
	 * of the specified age. 
	 */
	public static Date birthFloor(int ageInYears)
	{
		long time = System.currentTimeMillis();
		time -= (ageInYears * 365L * MILLIS_IN_DAY);

		return new Date(time);
	}
	
	/**
	 * Find the cutoff date for a certain number of days ago. 
	 */
	public static Date calculateDaysAgoJoinedFloor(int daysAgoJoined)
	{
		long time = System.currentTimeMillis();
		time -= (daysAgoJoined * MILLIS_IN_DAY);
		
		return new Date(time);
	}

	/**
	 * Yes, java date handling *is* this shitty.
	 */
	public static Integer getAge(java.sql.Date birth) 
	{
		if (birth == null)
		{
			return null;
		}

		Calendar now = Calendar.getInstance();

		Calendar birthCal = Calendar.getInstance();
		birthCal.setTime(birth);
		 
		int age = now.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
		 
		boolean isMonthGreater = birthCal.get(Calendar.MONTH) >= now.get(Calendar.MONTH);
		 
		boolean isMonthSameButDayGreater = birthCal.get(Calendar.MONTH) == now.get(Calendar.MONTH) && 
										   birthCal.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH);

		if (isMonthGreater || isMonthSameButDayGreater)
		{
			age = age - 1;
		}
		return age;
	}
}

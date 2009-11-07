package unsuck.lang;


/**
 * Some utils for working with threads
 */
public class ThreadUtils
{
	/**
	 * Thread.sleep that doesn't throw a fucking checked exception
	 */
	public void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e) { throw new RuntimeException(e); }
	}
}
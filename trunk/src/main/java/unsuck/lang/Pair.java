package unsuck.lang;


/**
 * A trivial holder of two things.
 */
public class Pair<F, S>
{
	/** Public access to the value */
	public F first;
	
	/** Public access to the value */
	public S second;
	
	/** Creates a pair */
	public Pair(F first, S second)
	{
		this.first = first;
		this.second = second;
	}
}
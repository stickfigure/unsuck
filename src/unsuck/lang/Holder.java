package unsuck.lang;


/**
 * A trivial dataholder.  This is most useful for storing as the value
 * in a hashmap so that you don't need to reput in order to change the value.
 * 
 * There is an identical class javax.xml.ws.Holder, but that isn't whitelisted
 * on Google App Engine.
 */
public class Holder<T>
{
	/** Public access to the value */
	public T value;
	
	/** Creates a holder with a null value */
	public Holder() {}
	
	/** Creates a holder with an initial value */
	public Holder(T value) { this.value = value; }
}
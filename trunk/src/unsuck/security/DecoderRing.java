/*
 * $Id$
 */

package unsuck.security;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Secret decoder ring which allows us to sign and verify the signature
 * of remote calls.  Contains the shared secret.
 * 
 * @author Jeff Schnitzer
 */
public class DecoderRing
{
	/** The shared secret */
	private static final String KEY = "somemadeupbullshitkey";
	
	/** */
	protected SortedMap<String, Object> params = new TreeMap<String, Object>();
	
	/** Starts a decoder ring for a given method */
	public DecoderRing(String method)
	{
		this.params.put("_method", method);
	}
	
	/** Don't add a timestamp or signature */
	public void addParam(String key, Object value)
	{
		this.params.put(key, value);
	}
	
	/** Create a signature for the given timestamp */
	public String sign(long timestamp)
	{
		if (this.params.containsKey("_timestamp"))
			throw new IllegalStateException("You can only sign once");

		this.params.put("_timestamp", timestamp);
		this.params.put("_secret", KEY);
		
		// We turn this whole thing into a JSON representation and sign that
		
		return null;
	}
	
	/** Verify a signature, throwing IllegalArgumentException on failure */
	public void verify(long timestamp, String signature) throws IllegalArgumentException
	{
	}
}
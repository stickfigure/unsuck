/*
 * $Id$
 */

package unsuck.security;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Secret decoder ring which allows us to sign and verify the signature
 * of remote calls.
 * 
 * Depends on Jackson and DigestOutputStream (thus commons-codec).
 * 
 * @author Jeff Schnitzer
 */
public class DecoderRing
{
	/** */
	protected SortedMap<String, Object> params = new TreeMap<String, Object>();
	
	/** Starts a decoder ring with a secret */
	public DecoderRing(String secret)
	{
		this.params.put("_secret", secret);
	}
	
	/** */
	public void setMethod(String name)
	{
		this.params.put("_method", name);
	}
	
	/** Don't add a timestamp or signature */
	public void addParam(String key, Object value)
	{
		this.params.put(key, value);
	}
	
	/** Create a signature for the given timestamp */
	public String sign(long timestamp)
	{
		return this.digest(timestamp);
	}
	
	/** Verify a signature, throwing IllegalArgumentException on failure */
	public void verify(long timestamp, String signature) throws IllegalArgumentException
	{
		if (!signature.equals(this.digest(timestamp)))
			throw new IllegalArgumentException("Bad signature");
	}
	
	/**
	 * @return the digest of this thing after adding timestamp
	 */
	private String digest(long timestamp)
	{
		if (this.params.containsKey("_timestamp"))
			throw new IllegalStateException("You can only sign/verify once");

		this.params.put("_timestamp", timestamp);
		
		// We turn this whole thing into a JSON representation and sign that
		ObjectMapper mapper = new ObjectMapper();
		DigestOutputStream digester = new DigestOutputStream("SHA1");
		
		try
		{
			mapper.writeValue(digester, this.params);
		}
		catch (IOException e) { throw new RuntimeException(e); }
		
		return digester.getDigestHex();
	}
}
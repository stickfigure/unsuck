/*
 */

package unsuck.security;

import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;

import unsuck.io.HexUtils;
import unsuck.json.BetterObjectMapper;
import unsuck.lang.Utils;

/**
 * Decoder ring which allows us to sign and verify the signature of an arbitrary object.
 * Doesn't hide the info, just signs it.
 * 
 * @author Jeff Schnitzer
 */
public class SignatureRing<T>
{
	/** Simple mapper with default typing enabled so we can deserialize an arbitrary object */
	private static BetterObjectMapper mapper = new BetterObjectMapper();
	// This doesn't work
	//static {
	//	mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
	//}
	
	/** */
	Class<T> clazz;
	byte[] secret;
	long validDurationMillis;
	
	/** */
	public SignatureRing(Class<T> clazz, String secret, long validDurationMillis)
	{
		this.clazz = clazz;
		this.secret = Utils.getBytesUTF8(secret);
		this.validDurationMillis = validDurationMillis;
	}

	/**
	 * Decode the value, ensuring that the signature and the timestamp are valid
	 */
	public T decode(String encoded) throws IllegalArgumentException {
		// get the signature
		int ind = encoded.lastIndexOf('|');
		String sigHex = encoded.substring(ind+1);
		
		String hashed = encoded.substring(0, ind);
		
		ind = hashed.lastIndexOf('|');
		long timestamp = Long.parseLong(hashed.substring(ind+1));
		
		String json = hashed.substring(0, ind);
		
		byte[] sig = HexUtils.decode(sigHex);
		byte[] mac = CryptoUtils.macHmacSHA256(hashed, secret);
		
		if (!Arrays.equals(sig, mac))
			throw new IllegalArgumentException("Failed signature");
		
		T decoded = mapper.fromJSON(json, clazz);
		
		if (timestamp + validDurationMillis < System.currentTimeMillis())
			throw new IllegalArgumentException("Expired timestamp");
		
		return decoded;
	}
	
	/** 
	 * @return a String which is NOT guaranteed to be web-safe 
	 */
	public String encode(T encodeMe) {
		
		String jsonified = mapper.toJSON(encodeMe);
		String withTimestamp = jsonified + "|" + System.currentTimeMillis();
		
		byte[] digest = CryptoUtils.macHmacSHA256(withTimestamp, secret);
			
		return withTimestamp + "|" + Hex.encodeHexString(digest);
	}
}
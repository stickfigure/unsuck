/*
 */

package unsuck.security;

import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;

import unsuck.io.HexUtils;
import unsuck.json.BetterObjectMapper;

/**
 * Decoder ring which allows us to sign and verify the signature of an arbitrary object.
 * Doesn't hide the info, just signs it.  The format for a signed message is:
 * 
 * SIG|TIMESTAMP|JSON
 * 
 * It's important that the sig comes first because this will give us randomziation in the
 * first block if we decide to further encrypt with a block cypher.
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
	public SignatureRing(Class<T> clazz, byte[] secret, long validDurationMillis)
	{
		this.clazz = clazz;
		this.secret = secret;
		this.validDurationMillis = validDurationMillis;
	}

	/**
	 * Decode the value, ensuring that the signature and the timestamp are valid
	 */
	public T decode(String encoded) throws IllegalArgumentException {
		// Note:  can't use split() because there might be pipes in the payload
		int ind = encoded.indexOf('|');
		String sigHex = encoded.substring(0, ind);
		String hashed = encoded.substring(ind+1);
		
		ind = hashed.indexOf('|');
		long timestamp = Long.parseLong(hashed.substring(0, ind));
		String json = hashed.substring(ind+1);
		
		byte[] sig = HexUtils.decode(sigHex);
		byte[] mac = CryptoUtils.macHmacSHA256(hashed, secret);
		
		if (!Arrays.equals(sig, mac))
			throw new IllegalArgumentException("Failed signature");
		
		T decoded = mapper.fromJSON(json, clazz);
		
		if (System.currentTimeMillis() - timestamp > validDurationMillis)
			throw new IllegalArgumentException("Expired timestamp");
		
		return decoded;
	}
	
	/**
	 * Creates a string SIG|TIMESTAMP|JSON
	 * 
	 * @return a String which is NOT guaranteed to be web-safe 
	 */
	public String encode(T encodeMe) {
		
		String jsonified = mapper.toJSON(encodeMe);
		String withTimestamp = System.currentTimeMillis() + "|" + jsonified;
		
		byte[] digest = CryptoUtils.macHmacSHA256(withTimestamp, secret);
			
		return Hex.encodeHexString(digest) + "|" + withTimestamp;
	}
}
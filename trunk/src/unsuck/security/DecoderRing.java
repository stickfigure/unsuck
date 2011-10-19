/*
 */

package unsuck.security;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;

import unsuck.io.HexUtils;
import unsuck.lang.Utils;
import unsuck.web.URLUtils;

/**
 * Decoder ring which allows us to sign and verify the signature of an arbitrary Map<String, String>.
 * Doesn't hide the info, just signs it.
 * 
 * @author Jeff Schnitzer
 */
public class DecoderRing
{
	/** */
	private static final String TS_KEY = "_ts";
	
	/** */
	byte[] secret;
	long validDurationMillis;
	
	/** */
	public DecoderRing(String secret, long validDurationMillis)
	{
		this.secret = Utils.getBytesUTF8(secret);
		this.validDurationMillis = validDurationMillis;
	}

	/**
	 * Decode the value, ensuring that the signature and the timestamp are valid
	 */
	public Map<String, String> decode(String encoded) throws IllegalArgumentException {
		// strip off the & at the end
		int ind = encoded.lastIndexOf('&');
		
		String sigHex = encoded.substring(ind+1);
		String queryStr = encoded.substring(0, ind);
		
		byte[] sig = HexUtils.decode(sigHex);
		
		MessageDigest md = DigestUtils.createDigestSHA256();
		md.update(secret);
		byte[] digest = md.digest(queryStr.getBytes());
		
		if (!Arrays.equals(sig, digest))
			throw new IllegalArgumentException("Failed signature");
		
		Map<String, String> decoded = URLUtils.parseQueryString(queryStr);
		long timestamp = Long.parseLong(decoded.remove(TS_KEY));
		
		if (timestamp + validDurationMillis > System.currentTimeMillis())
			throw new IllegalArgumentException("Expired timestamp");
		
		return decoded;
	}
	
	/** 
	 * @return a String which is NOT guaranteed to be web-safe 
	 */
	public String encode(Map<String, String> encodeMe) {
		
		// Let's not modify the original map
		Map<String, Object> withTs = new HashMap<String, Object>(encodeMe);
		withTs.put(TS_KEY, System.currentTimeMillis());
		
		String queryStr = URLUtils.buildQueryString(withTs);
		
		MessageDigest md = DigestUtils.createDigestSHA256();
		md.update(secret);
		byte[] digest = md.digest(queryStr.getBytes());
			
		return queryStr + '&' + Hex.encodeHexString(digest);
	}
}
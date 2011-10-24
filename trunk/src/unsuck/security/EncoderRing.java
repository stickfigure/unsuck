/*
 */

package unsuck.security;

import org.apache.commons.codec.binary.Base64;

/**
 * Extends the SignatureRing to actually encrypted the contents with AES/CBC for privacy.
 * Instead of using an IV, we rely on the fact that the signature is the first few bytes
 * and the timestamp changes frequently.
 * 
 * @author Jeff Schnitzer
 */
public class EncoderRing<T> extends SignatureRing<T>
{
	/**
	 * @param secret needs to be exactly 16 bytes in length
	 */
	public EncoderRing(Class<T> clazz, byte[] secret, long validDurationMillis)
	{
		super(clazz, secret, validDurationMillis);
	}

	/**
	 * Decode the value, ensuring that the signature and the timestamp are valid.
	 */
	public T decode(String encoded) throws IllegalArgumentException {
		byte[] encrypted = Base64.decodeBase64(encoded);
		String unencrypted = CryptoUtils.decryptAES(encrypted, secret);
		return super.decode(unencrypted);
	}
	
	/** 
	 * @return a base64-urlsafe-encoded, encrypted, signed string 
	 */
	public String encode(T encodeMe) {
		String unencrypted = super.encode(encodeMe);
		byte[] encrypted = CryptoUtils.encryptAES(unencrypted, secret);
		return Base64.encodeBase64URLSafeString(encrypted);
	}
}
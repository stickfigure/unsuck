/*
 */

package unsuck.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Some static methods to help working with MessageDigest
 * 
 * @author Jeff Schnitzer
 */
public class DigestUtils
{
	/** Masks the annoying exceptions */
	public static MessageDigest createDigestSHA256() {
		return createDigest("SHA-256");
	}

	/** Masks the annoying exceptions */
	public static MessageDigest createDigest(String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
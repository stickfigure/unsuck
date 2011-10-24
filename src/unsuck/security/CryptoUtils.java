/*
 */

package unsuck.security;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import unsuck.lang.Utils;

/**
 * Some static methods to help working with MessageDigest
 * 
 * @author Jeff Schnitzer
 */
public class CryptoUtils
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

	/** Masks the annoying exceptions */
	public static Mac createMacHmacSHA256() {
		return createMac("HmacSHA256");
	}

	/** Masks the annoying exceptions */
	public static Mac createMac(String algorithm) {
		try {
			return Mac.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** */
	public static SecretKey createSecretKeyHmacSHA256(byte[] secret) {
		return new SecretKeySpec(secret, "HmacSHA256");
	}
	
	/** Create the mac code for a message and secret */
	public static byte[] macHmacSHA256(String msg, byte[] secret) {
		Mac mac = CryptoUtils.createMacHmacSHA256();
		
		try { mac.init(createSecretKeyHmacSHA256(secret)); }
		catch (InvalidKeyException ex) { throw new RuntimeException(ex); }
		
		return mac.doFinal(Utils.getBytesUTF8(msg));
	}
	
	/** */
	public static byte[] encryptAESCBC(String msg, byte[] secret) {
		return encrypt(msg, secret, "AES/CBC");
	}
	
	/** */
	public static byte[] encrypt(String msg, byte[] secret, String algorithm) {
		try {
			Cipher c = Cipher.getInstance(algorithm);
			c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret, algorithm));
			return c.doFinal(Utils.getBytesUTF8(msg));
		} catch (GeneralSecurityException ex) {
			throw new RuntimeException(ex);
		}
	}

	/** */
	public static String decryptAESCBC(byte[] cipherText, byte[] secret) {
		return decrypt(cipherText, secret, "AES/CBC");
	}
	
	/** */
	public static String decrypt(byte[] cipherText, byte[] secret, String algorithm) {
		try {
			Cipher c = Cipher.getInstance(algorithm);
			c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret, algorithm));
			return Utils.newStringUTF8(c.doFinal(cipherText));
		} catch (GeneralSecurityException ex) {
			throw new RuntimeException(ex);
		}
	}
}